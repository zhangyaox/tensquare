package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    FriendDao friendDao;

    public int addFriend(String userid, String friendid) {
        //首先先查看是否是好友  防止重复添加
        return 0;
    }
}
