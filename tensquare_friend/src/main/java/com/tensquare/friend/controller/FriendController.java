package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static javax.swing.text.html.CSS.getAttribute;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友或非好友
     * @param friendid
     * @param type
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){

        Claims claims = (Claims) request.getAttribute("claims_user");
        //验证用户的登录，获取useid
        if(claims == null){
            //没有user角色
            return  new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //获取userid
//        String userid = claims.getId();
        String userid ="2";
        System.out.println(claims);


        //判断添加好友还是非好友
        if(type != null){
            if(type.equals("1")){
                //添加好友
                int flag = friendService.addFriend(userid,friendid);
                if(flag==0){
                    return  new Result(false, StatusCode.ERROR,"不能重复添加好友");
                }
                if(flag==1){
                    userClient.incFanscount(userid,friendid,1);
                    return  new Result(true,StatusCode.OK,"添加成功");
                }
            }else if(type.equals("0")){
                //添加非好友
                int flag = friendService.addNoFriend(userid,friendid);
                if(flag==0){
                    return  new Result(false, StatusCode.ERROR,"不能重复添加非好友");
                }
                if(flag==1){
                    return  new Result(true,StatusCode.OK,"添加成功");
                }
            }
            return  new Result(false, StatusCode.ERROR,"参数错误");
        }else{
            return  new Result(false, StatusCode.ERROR,"参数错误");
        }
    }

    /**
     * 删除好友
     * @param friendid
     * @return
     */
    @RequestMapping(value="/{friendid}",method=RequestMethod.DELETE)
    public Result remove(@PathVariable String friendid){
        Claims claims=(Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"无权访问");
        }
        friendService.deleteFriend(claims.getId(), friendid);
        userClient.incFanscount(claims.getId(),friendid,-1);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
