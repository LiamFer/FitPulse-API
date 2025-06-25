//package com.liamfer.workoutTracker.service;
//
//import org.redisson.api.RBucket;
//import org.redisson.api.RedissonClient;
//import org.springframework.stereotype.Service;
//
//
//public class RedisService {
//    private final RedissonClient redissonClient;
//
//    public RedisService(RedissonClient redissonClient) {
//        this.redissonClient = redissonClient;
//    }
//
//    public void saveValue(String key, Object value) {
//        RBucket<Object> bucket = redissonClient.getBucket(key);
//        bucket.set(value);
//    }
//
//    public Object getValue(String key) {
//        RBucket<Object> bucket = redissonClient.getBucket(key);
//        return bucket.get();
//    }
//}
