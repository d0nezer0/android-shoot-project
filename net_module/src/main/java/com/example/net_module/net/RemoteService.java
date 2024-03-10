package com.example.net_module.net;


import com.example.common_module.db.mode.SingleShootDataModel;
import com.example.net_module.mode.InitModeData;
import com.example.net_module.mode.TopUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 功能   ：Retrofit2所有Service
 */

public interface RemoteService {

//    /**
//     * 注册请求的接口
//     *
//     * @param model 用户的注册信息
//     * @return 返回注册RspModel<AccountModel>
//     */
//    @POST("account/register")
//    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);
//
//    /**
//     * 登录请求的接口
//     *
//     * @param model 用户的登录请求信息
//     * @return 返回注册RspModel<AccountModel>
//     */
//    @POST("account/login")
//    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);
//
//    /**
//     * 绑定设备Id的接口
//     *
//     * @param pushId 设备Id
//     * @return 返回注册RspModel<AccountModel>
//     */
//    @POST("account/bind/{pushId}")
//    Call<RspModel<AccountRspModel>> accountBindId(@Path(encoded = true, value = "pushId") String pushId);
//
//    /**
//     * 用户更新用户信息的接口
//     *
//     * @param model 用户更新的参数Model
//     * @return UserCard (都是根据数据库的返回类型所决定)
//     */
//    @PUT("user")
//    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);
//
//    /**
//     * 搜索用户的接口
//     *
//     * @param name 输入的搜索的用户名
//     * @return 返回模糊匹配符合条件的List<UserCard>
//     */
//    @GET("user/search/{name}")
//    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);
//
//    @PUT("user/follow/{userId}")
//    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);
//
//    /**
//     * 查询联系人的接口
//     *
//     * @return 查询到的用户信息集合
//     */
//    @GET("user/contact")
//    Call<RspModel<List<UserCard>>> userContacts();
//
//    /**
//     * 查询某人信息的接口
//     *
//     * @return 查询到的用户信息
//     */
//    @GET("user/{userId}")
//    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);
//
//    /**
//     * 查询某人信息的接口
//     *
//     * @return 查询到的用户信息
//     */
//    @POST("msg")
//    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);
//
//    /**
//     * 查询为推送的消息
//     *
//     * @return 未推送的消息
//     */
//    @POST("msg/history")
//    Call<RspModel<List<MessageCard>>> pullMsg();
//
//    /**
//     * 创建群的方法
//     *
//     * @param model 群创建信息
//     * @return 创建完成的GroupCard
//     */
//    @POST("group")
//    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);
//
//    /**
//     * 根据你Id查找一个群
//     *
//     * @param groupId 群Id
//     * @return 返回
//     */
//    @GET("group/{groupId}")
//    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);
//
//    /**
//     * 根据群名称 模糊匹配搜索群 传入参数搜索最近20个创建的群
//     *
//     * @param name 群名称
//     * @return List<GroupCard>
//     */
//    @GET("group/search/{name}")
//    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);
//
//    /**
//     * 根据群日期 搜索指定日期之后创建的群 传入参数搜索最近20个创建的群
//     *
//     * @param date 日期
//     * @return List<GroupCard>
//     */
//    @GET("group/list/{date}")
//    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);
//
//    /**
//     * 拉群一个群的所有成员 你必须是群的成员才行
//     *
//     * @param groupId 群Id
//     * @return 成员列表
//     */
//    @GET("group/{groupId}/members")
//    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);
//
//    /**
//     * 给群添加成员的接口
//     *
//     * @param groupId 群Id 你必须是这个群成员之一
//     * @param model   添加成员
//     * @return 添加的成员列表
//     */
//    @POST("group/{groupId}/member")
//    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId, @Body GroupMemberAddModel model);
//
//    /**
//     * 更改成员信息，请求的人要么是管理员，要么就是成员本人
//     * 管理员可以修改成员的别名和权限 成员只能修改别名
//     *
//     * @param memberId 成员Id，可以查询对应的群，和人
//     * @param model    修改的Model
//     * @return 当前成员的信息
//     */
//    @PUT("group/member/{memberId}")
//    Call<RspModel<GroupMemberCard>> modifyMember(@Path("memberId") String memberId, @Body GroupMembeUpdateModel model);
//
//    /**
//     * 申请加入一个群，
//     * 此时会创建一个加入的申请，并写入表；然后会给管理员发送消息
//     * 管理员同意，其实就是调用添加成员的接口把对应的用户添加进去
//     *
//     * @param groupId 群Id
//     * @return 申请的信息
//     */
//    @POST("group/applyJoin/{groupId}")
//    Call<RspModel<ApplyCard>> groupJoin(@Path("groupId") String groupId, @Body GroupApplyModel model);

    @GET("fetch_shot_cfg/{uuid}")
    Call<InitModeData> init(@Path(value = "uuid") String uuid);

    @POST("shot_info")
    Call<String> postSingleData(@Body SingleShootDataModel model);

    @GET("fetch_ranks")
    Call<TopUser> getTopTypeList();
}
