/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.time.memory.gui.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.time.memory.R;
import com.time.memory.entity.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Desction:图片裁剪
 */
public class PhotoEditActivity extends PhotoBaseActivity {

	static final String TAKE_PHOTO_ACTION = "take_photo_action";
	private ArrayList<PhotoInfo> mPhotoInfo;//拍照信息
	private ProgressDialog mProgressDialog;

	private boolean isOld;//返回位置
	private boolean isAddMore;//补充
	private boolean isWriter;//写记忆
	private int state;//隐私权限
	private String groupId;//群Id

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (GalleryFinal.getFunctionConfig() == null) {
			resultFailureDelayed(getString(R.string.please_reopen_gf), true);
		} else {
			mPhotoInfo = new ArrayList<>();
			ButterKnife.bind(this);
			mTakePhotoAction = this.getIntent().getBooleanExtra(TAKE_PHOTO_ACTION, false);
			if (mTakePhotoAction) {
				//打开相机
				requestTakePermission();
				//获取数据
				getDate();
			}
		}
	}

	//获取数据
	private void getDate() {
		isOld = GalleryFinal.getFunctionConfig().isOld();
		isWriter = GalleryFinal.getFunctionConfig().isWriter();
		state = GalleryFinal.getFunctionConfig().getState();
		groupId = GalleryFinal.getFunctionConfig().getId();
		isAddMore = GalleryFinal.getFunctionConfig().isAddMore();
	}

	@Override
	protected void takeResult(PhotoInfo info) {
		mPhotoInfo.add(info);
		//确定
		if (!isOld) {
			if (!isAddMore) {
				//写记忆
				resultWriter(mPhotoInfo, state, groupId);
			} else {
				//补充记忆
				resultSuppory(mPhotoInfo, state, groupId);
			}
		} else {
			//原路返回
			resultData(mPhotoInfo);
		}
	}


	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
		takePhotoAction();
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝
	}
}
