package com.time.memory.entity;

/**
 * Created by Administrator on 2016/11/7.
 */
public class DataStatistics extends BaseEntity {
    /**
     * 我创建的圈子数
     */
    private String createGroupCount;
    /**
     * 我加入别人创建的圈子数
     */
    private  String joinGroupCount;
    /**
     * 我发布的记忆数
     */
    private String createMemoryCount;
    /**
     * 我补充别人的记忆数
     */
    private  String addMemoryPointCount1;
    /**
     * 别人补充我的记忆数
     */
    private  String addMemoryPointCount2;
    /**
     * 我的好友数
     */
    private  String friendCount;

    public String getCreateGroupCount() {
        return createGroupCount;
    }

    public void setCreateGroupCount(String createGroupCount) {
        this.createGroupCount = createGroupCount;
    }

    public String getJoinGroupCount() {
        return joinGroupCount;
    }

    public void setJoinGroupCount(String joinGroupCount) {
        this.joinGroupCount = joinGroupCount;
    }

    public String getCreateMemoryCount() {
        return createMemoryCount;
    }

    public void setCreateMemoryCount(String createMemoryCount) {
        this.createMemoryCount = createMemoryCount;
    }

    public String getAddMemoryPointCount1() {
        return addMemoryPointCount1;
    }

    public void setAddMemoryPointCount1(String addMemoryPointCount1) {
        this.addMemoryPointCount1 = addMemoryPointCount1;
    }

    public String getAddMemoryPointCount2() {
        return addMemoryPointCount2;
    }

    public void setAddMemoryPointCount2(String addMemoryPointCount2) {
        this.addMemoryPointCount2 = addMemoryPointCount2;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }
}
