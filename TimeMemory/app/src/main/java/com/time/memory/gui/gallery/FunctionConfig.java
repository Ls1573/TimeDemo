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

import android.support.annotation.IntRange;

import com.time.memory.entity.PhotoInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FunctionConfig implements Cloneable {

	protected boolean mutiSelect;
	//	protected int maxSize;
	protected boolean editPhoto;//编辑
	protected boolean crop;//裁剪
	private boolean rotate;//旋转
	private boolean isWriter;//书写

	public void setIsWriter(boolean isWriter) {
		this.isWriter = isWriter;
	}

	private boolean camera;
	private String memoryId;
	private String memorySourceId;
	private String userid;
	private int state;//隐私状态
	private int cropWidth;
	private int cropHeight;
	private boolean isAddMore;//更多
	private boolean isMore;//继续
	private int maxSize;
	private boolean cropSquare;
	private String cla;
	private boolean isOld;
	private String Id;
	private boolean rotateReplaceSource;//旋转是否覆盖源文件
	private boolean cropReplaceSource;//裁剪是否覆盖源文件
	private boolean forceCrop;//强制裁剪
	private boolean forceCropEdit;//强制裁剪后是否可对图片编辑，默认不可以
	private ArrayList<String> selectedList;
	private ArrayList<String> filterList;//过滤器

	private FunctionConfig(final Builder builder) {
		this.mutiSelect = builder.mutiSelect;
		this.maxSize = builder.maxSize;
		this.editPhoto = builder.editPhoto;
		this.crop = builder.crop;
		this.isMore = builder.isMore;
		this.memoryId = builder.memoryId;
		this.memorySourceId = builder.memorySourceId;
		this.userid = builder.userid;
		this.isAddMore = builder.isAddMore;
		this.cla = builder.cla;
		this.isWriter = builder.isWriter;
		this.rotate = builder.rotate;
		this.camera = builder.camera;
		this.cropWidth = builder.cropWidth;
		this.state = builder.state;
		this.Id = builder.Id;
		this.isOld = builder.isOld;
		this.cropHeight = builder.cropHeight;
		this.cropSquare = builder.cropSquare;
		this.selectedList = builder.selectedList;
		this.filterList = builder.filterList;
		this.rotateReplaceSource = builder.rotateReplaceSource;
		this.cropReplaceSource = builder.cropReplaceSource;
		this.forceCrop = builder.forceCrop;
		this.forceCropEdit = builder.forceCropEdit;
	}

	public static class Builder {
		private boolean mutiSelect;
		private int maxSize;
		private boolean editPhoto;//编辑
		private boolean crop;//裁剪
		private boolean rotate;//旋转
		private boolean camera;
		private boolean isAddMore;//追加
		private boolean isMore;//继续
		private String cla;
		private int cropWidth;
		private int cropHeight;
		private boolean cropSquare;
		private boolean rotateReplaceSource;//旋转是否覆盖源文件
		private boolean cropReplaceSource;//裁剪是否覆盖源文件
		private ArrayList<String> selectedList;
		private ArrayList<String> filterList;
		private boolean forceCrop;//强制裁剪
		private boolean forceCropEdit;//强制裁剪后是否可对图片编辑，默认不可以
		private boolean preview;//预览
		private boolean isOld;//原来页面
		private boolean isWriter;//写记忆
		private int state;//隐私状态
		private String Id;//群Id
		private String memoryId;
		private String memorySourceId;
		private String userid;


		public Builder setMutiSelect(boolean mutiSelect) {
			this.mutiSelect = mutiSelect;
			return this;
		}

		public Builder setWriter(boolean isWriter) {
			this.isWriter = isWriter;
			return this;
		}


		public Builder setMemoryId(String memoryId) {
			this.memoryId = memoryId;
			return this;
		}


		public Builder setMemorySourceId(String memorySourceId) {
			this.memorySourceId = memorySourceId;
			return this;
		}

		public Builder setUserId(String userid) {
			this.userid = userid;
			return this;
		}


		public Builder setAddMore(boolean isAddMore) {
			this.isAddMore = isAddMore;
			return this;
		}


		public Builder setIsMore(boolean isMore) {
			this.isMore = isMore;
			return this;
		}

		public Builder setState(int state) {
			this.state = state;
			return this;
		}

		public Builder setId(String Id) {
			this.Id = Id;
			return this;
		}


		public Builder setClass(String... clas) {
			if (clas != null && clas.length != 0)
				this.cla = clas[0];
			else
				this.cla = "";
			return this;
		}

		public Builder setMutiSelectMaxSize(@IntRange(from = 1, to = Integer.MAX_VALUE) int maxSize) {
			this.maxSize = maxSize;
			return this;
		}

		public Builder setEnableEdit(boolean enable) {
			this.editPhoto = enable;
			return this;
		}

		public Builder setOld(boolean isOld) {
			this.isOld = isOld;
			return this;
		}

		public Builder setEnableCrop(boolean enable) {
			this.crop = enable;
			return this;
		}

		public Builder setEnableRotate(boolean enable) {
			this.rotate = enable;
			return this;
		}

		public Builder setEnableCamera(boolean enable) {
			this.camera = enable;
			return this;
		}

		public Builder setCropWidth(@IntRange(from = 1, to = Integer.MAX_VALUE) int width) {
			this.cropWidth = width;
			return this;
		}

		public Builder setCropHeight(@IntRange(from = 1, to = Integer.MAX_VALUE) int height) {
			this.cropHeight = height;
			return this;
		}

		public Builder setCropSquare(boolean enable) {
			this.cropSquare = enable;
			return this;
		}

		public Builder setSelected(ArrayList<String> selectedList) {
			if (selectedList != null) {
				this.selectedList = (ArrayList<String>) selectedList.clone();
			}
			return this;
		}

		public Builder setSelected(Collection<PhotoInfo> selectedList) {
			if (selectedList != null) {
				ArrayList<String> list = new ArrayList<>();
				for (PhotoInfo info : selectedList) {
					if (info != null) {
						list.add(info.getPhotoPath());
					}
				}

				this.selectedList = list;
			}
			return this;
		}

//		public Builder setFilter(ArrayList<String> filterList) {
//			if (filterList != null) {
//				this.filterList = (ArrayList<String>) filterList.clone();
//			}
//			return this;
//		}
//
//		public Builder setFilter(Collection<PhotoInfo> filterList) {
//			if (filterList != null) {
//				ArrayList<String> list = new ArrayList<>();
//				for (PhotoInfo info : filterList) {
//					if (info != null) {
//						list.add(info.getPhotoPath());
//					}
//				}
//				this.filterList = list;
//			}
//			return this;
//		}

		public Builder setCommon(int maxSize, boolean isOld, boolean isWriter, boolean isAddMore, boolean isContinue, int state, String Id, List<PhotoInfo> selectedList, String... cla) {
			//最多1张
			setMutiSelectMaxSize(maxSize);
			//不可编辑
			setEnableEdit(false);
			//不可旋转
			setEnableRotate(false);
			//没有相机
			setEnableCamera(false);
			//可预览
			setEnablePreview(true);
			//添加过滤集合
			setSelected(selectedList);
			//设置回到位置
			setOld(isOld);
			//写记忆
			setWriter(isWriter);
			//隐私设置
			setState(state);
			//群Id
			setId(Id);
			//追加记忆设置
			setAddMore(isAddMore);
			//追加显示继续
			setIsMore(isContinue);
			//类名
			setClass(cla);
			return this;
		}

		public Builder setCommon(int maxSize, boolean isOld, boolean isWriter, boolean isAddMore, int state, String Id, String memoryId, String memorySourceId, String userId, List<PhotoInfo> selectedList) {
			//最多1张
			setMutiSelectMaxSize(maxSize);
			//不可编辑
			setEnableEdit(false);
			//不可旋转
			setEnableRotate(false);
			//没有相机
			setEnableCamera(false);
			//可预览
			setEnablePreview(true);
			//添加过滤集合
			setSelected(selectedList);
			//设置回到位置
			setOld(isOld);
			//写记忆
			setWriter(isWriter);
			//隐私设置
			setState(state);
			//隐私设置
			setId(Id);
			//追加记忆设置
			setAddMore(isAddMore);
			//记忆Id
			setMemoryId(memoryId);
			//记忆源Id
			setMemorySourceId(memorySourceId);
			//记忆发布人Id
			setUserId(userId);
			return this;
		}

		/**
		 * 设置旋转后是否替换原图
		 *
		 * @param rotateReplaceSource
		 * @return
		 */
		public Builder setRotateReplaceSource(boolean rotateReplaceSource) {
			this.rotateReplaceSource = rotateReplaceSource;
			return this;
		}

		/**
		 * 设置裁剪后是否替换原图
		 *
		 * @param cropReplaceSource
		 * @return
		 */
		public Builder setCropReplaceSource(boolean cropReplaceSource) {
			this.cropReplaceSource = cropReplaceSource;
			return this;
		}

		/**
		 * 强制裁剪
		 *
		 * @param forceCrop
		 * @return
		 */
		public Builder setForceCrop(boolean forceCrop) {
			this.forceCrop = forceCrop;
			return this;
		}

		/**
		 * 强制裁剪后是否可以对图片编辑，默认不可编辑
		 *
		 * @param forceCropEdit
		 * @return
		 */
		public Builder setForceCropEdit(boolean forceCropEdit) {
			this.forceCropEdit = forceCropEdit;
			return this;
		}

		/**
		 * 是否开启预览功能
		 *
		 * @param preview
		 * @return
		 */
		public Builder setEnablePreview(boolean preview) {
			this.preview = preview;
			return this;
		}

		public FunctionConfig build() {
			return new FunctionConfig(this);
		}
	}

	public boolean isMutiSelect() {
		return mutiSelect;
	}

//	public int getMaxSize() {
//		return maxSize;
//	}

	public boolean isEditPhoto() {
		return editPhoto;
	}

	public boolean isCrop() {
		return crop;
	}

	public boolean isRotate() {
		return rotate;
	}

	public boolean isCamera() {
		return camera;
	}

	public boolean isAddMore() {
		return isAddMore;
	}

	public boolean isMore() {
		return isMore;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public int getState() {
		return state;
	}

	public boolean isOld() {
		return isOld;
	}

	public String getId() {
		return Id;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public String getMemorySourceId() {
		return memorySourceId;
	}

	public String getUserId() {
		return userid;
	}

	public boolean isCropSquare() {
		return cropSquare;
	}

	public boolean isWriter() {
		return isWriter;
	}

	public boolean isRotateReplaceSource() {
		return rotateReplaceSource;
	}

	public boolean isCropReplaceSource() {
		return cropReplaceSource;
	}

	public boolean isForceCrop() {
		return forceCrop;
	}

	public boolean isForceCropEdit() {
		return forceCropEdit;
	}

	public String getOprClass() {
		return cla;
	}


	public ArrayList<String> getSelectedList() {
		return selectedList;
	}

	public ArrayList<String> getFilterList() {
		return filterList;
	}


	@Override
	public FunctionConfig clone() {
		FunctionConfig o = null;
		try {
			o = (FunctionConfig) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
