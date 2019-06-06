package com.elintminds.mac.metatopos.common;

import com.elintminds.mac.metatopos.beans.addcomment.AddCommentResponse;
import com.elintminds.mac.metatopos.beans.addpost.AddNewPostResponse;
import com.elintminds.mac.metatopos.beans.addremocefavoritepost.AddRemoveFvrtPostResponse;
import com.elintminds.mac.metatopos.beans.advertisementplan.AdvertisementPlanResponse;
import com.elintminds.mac.metatopos.beans.editprofile.FollowResponse;
import com.elintminds.mac.metatopos.beans.favouritepost.FavouritePostResponse;
import com.elintminds.mac.metatopos.beans.feedback.FeedbackResponse;
import com.elintminds.mac.metatopos.beans.fileupload.FileupoladResponse;
import com.elintminds.mac.metatopos.beans.forgotPassword.ForgotPasswordResponse;
import com.elintminds.mac.metatopos.beans.genEmoji.GemEmojiResponse;
import com.elintminds.mac.metatopos.beans.getNotifications.NotificationsResponse;
import com.elintminds.mac.metatopos.beans.getNotifications.ReadUnreadNotificationResponse;
import com.elintminds.mac.metatopos.beans.getadvertisementbyid.GetAdvertisemnetByIdResponse;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllUserPostResponse;
import com.elintminds.mac.metatopos.beans.getchatthreads.ChatThreadsResponse;
import com.elintminds.mac.metatopos.beans.getevents.GetEventByIdResponse;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.SearchFilterResponse;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersResponse;
import com.elintminds.mac.metatopos.beans.getmessges.GetMessgesResponse;
import com.elintminds.mac.metatopos.beans.getnotificationtype.GetNotificationTypeResponse;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdResponse;
import com.elintminds.mac.metatopos.beans.login.LoginResponse;
import com.elintminds.mac.metatopos.beans.login.ReportIssueResponse;
import com.elintminds.mac.metatopos.beans.register.RegisterResponse;
import com.elintminds.mac.metatopos.beans.register.UpdateLanguageResponse;
import com.elintminds.mac.metatopos.beans.removeUploadedFile.RemovefileResponse;
import com.elintminds.mac.metatopos.beans.removechathread.RemoveChatThread;
import com.elintminds.mac.metatopos.beans.removepost.RemovePostResponse;
import com.elintminds.mac.metatopos.beans.savepost.SavePostResponse;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageResponse;
import com.elintminds.mac.metatopos.beans.sendmessage.SendMessageResponse;
import com.elintminds.mac.metatopos.beans.sociallogin.SocialLoginResponse;
import com.elintminds.mac.metatopos.beans.updatesocialloginuserprofile.UpdateSocialLoginUserProfileResponse;
import com.elintminds.mac.metatopos.beans.useradvertisement.UserAdvertisementResponse;
import com.elintminds.mac.metatopos.beans.userevents.UserEventsResponse;
import com.elintminds.mac.metatopos.beans.userposts.UserPostResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {

    String BASE_URL = "https://metatopos.elintminds.work/";
    String BASE_URL_API = "https://metatopos.elintminds.work/api/";

    @POST("app_configuration")
    Call<SelectLanguageResponse> selectLanguageResponse();

    @POST("login")
    Call<LoginResponse> LoginResponse(@Body HashMap<String, String> map);

    @POST("sociallogin")
    Call<SocialLoginResponse> Sociallogin(@Body HashMap<String, String> map);

    @POST("register")
    Call<RegisterResponse> RegisterResponse(@Body HashMap<String, String> map);

    @POST("getgenmojis")
    Call<GemEmojiResponse> GenmojiRespose();

    @POST("getfeaturedgenmojis")
    Call<GemEmojiResponse> FeaturedGenmojiRespose();

    @POST("forgotpassword")
    Call<ForgotPasswordResponse> ForgotResponse(@Body HashMap<String, String> map);

    @POST("changepassword")
    Call<LoginResponse> changePassword(@Header("Token") String token,
                                       @Body HashMap<String, String> map);

    @Multipart
    @POST("fileupload")
    Call<FileupoladResponse> FileUploadResponse(@Part MultipartBody.Part file,
                                                @Part("filetype") RequestBody fileType,
                                                @Part("previouspath") RequestBody previousPath);

    @POST("removeuploadfile")
    Call<RemovefileResponse> removeFile(@Body HashMap<String, String> map);

    @POST("updateuserlanguage")
    Call<UpdateLanguageResponse> updateLanguage(@Body HashMap<String, String> map);

    @GET("userprofile")
    Call<LoginResponse> getUserProfile(@Header("Token") String token);

    @POST("getpostsbylatlng")
    Call<AllUserPostResponse> getAllUserPosts(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getcategorieslist")
    Call<AddNewPostResponse> getPostCategoryList();


    @POST("post/savepost")
    Call<SavePostResponse> savePost(@Header("Token") String token, @Body HashMap<String, Object> map);


    @GET("userprofile/{userid}")
    Call<LoginResponse> getotherUerProfile(@Header("Token") String token,
                                           @Path("userid") String userid);

    @POST("followorunfollowuser")
    Call<FollowResponse> followUnfollowUser(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getpostsbyuserid")
    Call<UserPostResponse> getPosts(@Header("Token") String token);

    @POST("getpostsbyuserid")
    Call<UserPostResponse> getotherUserPosts(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("geteventsbyuserid")
    Call<UserEventsResponse> getEvents(@Header("Token") String token);

    @POST("geteventsbyuserid")
    Call<UserEventsResponse> getotherUserEvents(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getadvertisementsbyuserid")
    Call<UserAdvertisementResponse> getAdvertisement(@Header("Token") String token);

    @POST("getadvertisementsbyuserid")
    Call<UserAdvertisementResponse> getotherUserAdvertisement(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getfavouriteposts")
    Call<FavouritePostResponse> getfavouriteposts(@Header("Token") String token);

    @POST("savereportissues")
    Call<ReportIssueResponse> reportIssue(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("editprofile")
    Call<ReportIssueResponse> editProfile(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("removeprofilephoto")
    Call<ReportIssueResponse> removeProfilePhoto(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getfeedbacksbyuserid")
    Call<FeedbackResponse> getFeedbacks(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("addfeedback")
    Call<FeedbackResponse> giveFeedback(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getusersubscriptionplans")
    Call<AdvertisementPlanResponse> getUserAdvertisementPlan(@Header("Token") String token);
    @POST("getpostbyid")
    Call<GetPostByIdResponse> getPostByID(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getpostbyid")
    Call<GetEventByIdResponse> getEventByID(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getpostbyid")
    Call<GetAdvertisemnetByIdResponse> getAdvertisemntByID(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("addorremovefavouritepost")
    Call<AddRemoveFvrtPostResponse> addRemoveFvrtPost(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("sendmessage")
    Call<SendMessageResponse> sendMessage(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("addinterestedornotforevent")
    Call<AddRemoveFvrtPostResponse> intrestedForEventorNot(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("post/addcomment")
    Call<AddCommentResponse> addComment(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getfiltercategorieslist")
    Call<SearchFilterResponse> searchFilterCategoryList(@Header("Token") String token);

    @POST("search/getposts")
    Call<AllUserPostResponse> searchFilter(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("removepostbyid")
    Call<RemovePostResponse> removePost(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("logout")
    Call<LoginResponse> logout(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getfollowersbyuserid")
    Call<FollowersResponse> getfollowersList(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getfollowingsbyuserid")
    Call<FollowersResponse> getFollowingList(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getpostsbysearchfilters")
    Call<AllUserPostResponse> applyFiltersbyCategoryID(@Header("Token") String token, @Body HashMap<String, Object> map);

    @POST("updateuserphoto")
    Call<UpdateSocialLoginUserProfileResponse> updateUserPhoto(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getmessages")
    Call<GetMessgesResponse> getMessages(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getchatthreads")
    Call<ChatThreadsResponse> getChatThreads(@Header("Token") String token);

    @POST("removechatbychatid")
    Call<RemoveChatThread> removeChatThread(@Header("Token") String token, @Body HashMap<String, String> map);


    @POST("usersettings/changesettings")
    Call<LoginResponse> changeUserSettings(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getnotifications")
    Call<NotificationsResponse> getNotifications(@Header("Token") String token);

    @POST("changenotificationstatus")
    Call<ReadUnreadNotificationResponse> readUnreadNotification(@Header("Token") String token, @Body HashMap<String, String> map);

    @POST("getnotificationstype")
    Call<GetNotificationTypeResponse> getNotificationTypes(@Header("Token") String token);

    @POST("removenotification")
    Call<ReadUnreadNotificationResponse> removeNotification(@Header("Token") String token, @Body HashMap<String, String> map);


}
