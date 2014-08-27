/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityDataMapper;
import com.fernandocejas.android10.sample.data.repository.UserDataRepository;
import com.fernandocejas.android10.sample.data.repository.datasource.UserDataStoreFactory;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListUseCaseImpl;
import com.fernandocejas.android10.sample.domain.repository.UserRepository;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.fernandocejas.android10.sample.presentation.presenter.UserListPresenter;
import com.fernandocejas.android10.sample.presentation.view.UserListView;
import java.util.Collection;

/**
 * Fragment that shows a list of Users.
 */
public class UserListFragment extends BaseFragment implements UserListView {

  private UserListPresenter userListPresenter;

  private RelativeLayout rl_progress;
  private RelativeLayout rl_retry;
  private Button bt_retry;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View fragmentView = inflater.inflate(R.layout.fragment_user_list, container, true);

    this.rl_progress = (RelativeLayout) fragmentView.findViewById(R.id.rl_progress);
    this.rl_retry = (RelativeLayout) fragmentView.findViewById(R.id.rl_retry);
    this.bt_retry = (Button) fragmentView.findViewById(R.id.bt_retry);
    this.bt_retry.setOnClickListener(retryOnClickListener);

    return fragmentView;
  }

  @Override public void onResume() {
    super.onResume();
    this.userListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.userListPresenter.pause();
  }

  @Override protected void initializePresenter() {
    // All these dependency initialization could have been avoided using a
    // dependency injection framework. But in this case are used this way for
    // learning example purpose.
    UserDataStoreFactory userDataStoreFactory = new UserDataStoreFactory(this.getContext());
    UserEntityDataMapper userEntityDataMapper = new UserEntityDataMapper();
    UserRepository userRepository = UserDataRepository.getInstance(userDataStoreFactory,
        userEntityDataMapper);
    GetUserListUseCase getUserListUseCase = new GetUserListUseCaseImpl(userRepository);
    this.userListPresenter = new UserListPresenter(this, getUserListUseCase);
  }

  @Override public void showLoading() {
    this.rl_progress.setVisibility(View.VISIBLE);
    this.getActivity().setProgressBarIndeterminateVisibility(true);
  }

  @Override public void hideLoading() {
    this.rl_progress.setVisibility(View.GONE);
    this.getActivity().setProgressBarIndeterminateVisibility(false);
  }

  @Override public void showRetry() {
    this.rl_retry.setVisibility(View.VISIBLE);
  }

  @Override public void hideRetry() {
    this.rl_retry.setVisibility(View.GONE);
  }

  @Override public void renderUserList(Collection<UserModel> userModelCollection) {

  }

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

  @Override public Context getContext() {
    return this.getActivity().getApplicationContext();
  }

  /**
   * Shows a {@link android.widget.Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  private void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  /**
   * Loads all users.
   */
  private void loadUserList() {
    if (this.userListPresenter != null) {
      this.userListPresenter.loadUserList();
    }
  }

  final View.OnClickListener retryOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View view) {
      UserListFragment.this.loadUserList();
    }
  };
}