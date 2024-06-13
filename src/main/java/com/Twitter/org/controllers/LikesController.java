package com.Twitter.org.controllers;

import com.Twitter.org.Models.Response;
import com.Twitter.org.Models.Tweets.Tweets;
import com.Twitter.org.Models.dto.TweetsDto;
import com.Twitter.org.Models.dto.UserDto.UserResponseDto;
import com.Twitter.org.mappers.Impl.TweetsMapper;
import com.Twitter.org.mappers.Impl.UserMapper.UserResponseMapper;
import com.Twitter.org.services.Likes.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikesController {

    private final LikesService likesService;
    private final TweetsMapper tweetsMapper;
    private final UserResponseMapper userResponseMapper;


    public LikesController(LikesService likesService, TweetsMapper tweetsMapper, UserResponseMapper userResponseMapper) {
        this.likesService = likesService;
        this.tweetsMapper = tweetsMapper;
        this.userResponseMapper = userResponseMapper;
    }


    // User likes a tweet
    @PostMapping("/{username}/tweets/{tweetId}/likes")
    public ResponseEntity<Response> likeTweet(@PathVariable String username, @PathVariable int tweetId) {
        Response response = likesService.addLike(username, tweetId);
        if (response.isSuccess()) {
            // Map the Tweets object to a TweetsDto object
            TweetsDto tweetDto = tweetsMapper.mapTo((Tweets) response.getData());
            // Set the TweetsDto object to the response data
            response.setData(tweetDto);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // User unlikes a tweet
    @DeleteMapping("/{username}/tweets/{tweetId}/likes")
    public ResponseEntity<Response> unlikeTweet(@PathVariable String username, @PathVariable int tweetId) {
        Response response = likesService.removeLike(username, tweetId);
        if (response.isSuccess()) {
            TweetsDto tweetDto = tweetsMapper.mapTo((Tweets) response.getData());
            response.setData(tweetDto);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // Check if a user has liked a tweet
    @GetMapping("/{username}/tweets/{tweetId}/likes")
    public boolean isLiked(@PathVariable String username, @PathVariable int tweetId) {
        return likesService.isLiked(username, tweetId);
    }

    // Count the number of likes for a tweet
    @GetMapping("/tweets/{tweetId}/likes/count")
    public int countLikes(@PathVariable int tweetId) {
        return likesService.countLikes(tweetId);
    }

    // Get all tweets liked by a user
    @GetMapping("/{username}/likes/tweets")
    public List<TweetsDto> getLikedTweets(@PathVariable String username) {
        List<Tweets> likedTweets = likesService.getLikedTweets(username);
        List<TweetsDto> likedTweetsDtos;
        likedTweetsDtos = likedTweets.stream()
                .map(tweetsMapper::mapTo)
                .toList();
        return likedTweetsDtos;
    }

    // Get all users who liked a tweet
    @GetMapping("/tweets/{tweetId}/likes/users")
    public List<UserResponseDto> getUsersWhoLikedTweet(@PathVariable int tweetId) {
        List<UserResponseDto> users;
        users = likesService.getUsersWhoLikedTweet(tweetId).stream()
                .map(userResponseMapper::mapTo)
                .toList();
        return users;
    }
}
