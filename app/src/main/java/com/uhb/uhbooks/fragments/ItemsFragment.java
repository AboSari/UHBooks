package com.uhb.uhbooks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.uhb.uhbooks.App;
import com.uhb.uhbooks.PhotoViewActivity;
import com.uhb.uhbooks.R;
import com.uhb.uhbooks.Utils.Utils;
import com.uhb.uhbooks.adapters.ItemAdapter;
import com.uhb.uhbooks.models.Api;
import com.uhb.uhbooks.models.File;
import com.uhb.uhbooks.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFragment extends mainFragment implements ItemAdapter.OnItemListener {

    private static final String TAG = "ItemsFragment";
    private static final String KEY_LEVEL = "level";

    private String mLevel;
    private List<Item> mItems;
    private ItemAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<Integer> mDepth;

    public static ItemsFragment getInstance(Item.Level level) {
        ItemsFragment fragment = new ItemsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_LEVEL, level.name());
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getString(KEY_LEVEL);
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mDepth.size() > 0) {
                    mDepth.remove(mDepth.size() - 1);
                    List<Item> items = mItems;
                    for (int index : mDepth) items = items.get(index).getItems();
                    mAdapter.notifyChanged(items);
                } else if (isEnabled()) {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        recyclerView = view.findViewById(R.id.rvItems);

        getItems();
        return view;
    }

    public void getItems() {
        loading(true);
        mCall = ((App) getActivity().getApplication()).getApiInterface()
                .getItems(mLevel, ((App) getActivity().getApplication()).getSession().getToken());
        mCall.enqueue(new Callback<Api>() {
            @Override
            public void onResponse(Call<Api> call, Response<Api> response) {
                loading(false);
                if (response.isSuccessful()) {
                    mItems = response.body().getItems();
                    mDepth = new ArrayList<>();
                    setRecyclerView();
                } else {
                    Utils.showResponseFailedDialog(getActivity(), response);
                }
            }

            @Override
            public void onFailure(Call<Api> call, Throwable t) {
                loading(false);
                if (call.isCanceled()) return;
                Utils.showRetrofitFailureDialog(getActivity(), t);
            }
        });

    }

    private void setRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new ItemAdapter(getActivity(), mItems, this);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyChanged(mItems);
        }
    }

    private void viewFile(File file) {
        Gson gson = new Gson();
        String json = gson.toJson(file);

        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.IMAGES, json);

        startActivity(intent);
    }

    @Override
    public void onItemClick(int i) {
        Item item;
        if (mDepth.size() == 0) {
            item = mItems.get(i);
        } else {
            List<Item> items = mItems;
            for (int index : mDepth) items = items.get(index).getItems();
            item = items.get(i);
        }

        if (item.getType() == Item.Type.FOLDER) {
            mAdapter.notifyChanged(item.getItems());
            mDepth.add(i);
        } else {
            loading(true);
            mCall = ((App) getActivity().getApplication()).getApiInterface()
                    .getImages(item.getPath(), ((App) getActivity().getApplication()).getSession().getToken());
            mCall.enqueue(new Callback<Api>() {
                @Override
                public void onResponse(Call<Api> call, Response<Api> response) {
                    loading(false);
                    if (response.isSuccessful()) {
                        viewFile(new File(response.body().getImages()));
                    } else {
                        Utils.showResponseFailedDialog(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<Api> call, Throwable t) {
                    loading(false);
                    if (call.isCanceled()) return;
                    Utils.showRetrofitFailureDialog(getActivity(), t);
                }
            });
        }
    }
}

