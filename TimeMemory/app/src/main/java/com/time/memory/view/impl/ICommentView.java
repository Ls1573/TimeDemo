package com.time.memory.view.impl;

import com.time.memory.entity.Comment;
import com.time.memory.entity.Fork;

import java.util.List;

/**
 * V-P
 */
public interface ICommentView extends IBaseView {

	void setAdapter(List<Comment> CList,List<Fork> FList);
}
