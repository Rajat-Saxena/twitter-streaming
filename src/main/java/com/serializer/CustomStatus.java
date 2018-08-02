package com.serializer;

import twitter4j.*;

import java.util.Date;

public class CustomStatus {
    String text;

    Date createdAt;

    User user;

    long id;

    int displayTextRangeStart;

    int displayTextRangeEnd;

    String source;

    boolean isTruncated;

    long inReplyToStatusId;

    long inReplyToUserId;

    String inReplyToScreenName;

    GeoLocation geoLocation;

    Place place;

    boolean isFavorited;

    boolean isRetweeted;

    int favoriteCount;

    boolean isRetweet;

    Status retweetedStatus;

    long[] contributors;

    int retweetCount;

    boolean isRetweetedByMe;

    long currentUserRetweetId;

    boolean isPossiblySensitive;

    String lang;

    Scopes scopes;

    String[] withheldInCountries;

    long quotedStatusId;

    Status quotedStatus;

    RateLimitStatus rateLimitStatus;

    int accessLevel;

    UserMentionEntity[] userMentionEntities;

    URLEntity[] URLEntities;
    HashtagEntity[] hashtagEntities;
    MediaEntity[] mediaEntities;
    SymbolEntity[] symbolEntities;

    public URLEntity[] getURLEntities() {
        return URLEntities;
    }

    public void setURLEntities(URLEntity[] URLEntities) {
        this.URLEntities = URLEntities;
    }

    public HashtagEntity[] getHashtagEntities() {
        return hashtagEntities;
    }

    public void setHashtagEntities(HashtagEntity[] hashtagEntities) {
        this.hashtagEntities = hashtagEntities;
    }

    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    public void setMediaEntities(MediaEntity[] mediaEntities) {
        this.mediaEntities = mediaEntities;
    }

    public SymbolEntity[] getSymbolEntities() {
        return symbolEntities;
    }

    public void setSymbolEntities(SymbolEntity[] symbolEntities) {
        this.symbolEntities = symbolEntities;
    }

    public UserMentionEntity[] getUserMentionEntities() {
        return userMentionEntities;
    }

    public void setUserMentionEntities(UserMentionEntity[] userMentionEntities) {
        this.userMentionEntities = userMentionEntities;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    public void setRateLimitStatus(RateLimitStatus rateLimitStatus) {
        this.rateLimitStatus = rateLimitStatus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDisplayTextRangeStart() {
        return displayTextRangeStart;
    }

    public void setDisplayTextRangeStart(int displayTextRangeStart) {
        this.displayTextRangeStart = displayTextRangeStart;
    }

    public int getDisplayTextRangeEnd() {
        return displayTextRangeEnd;
    }

    public void setDisplayTextRangeEnd(int displayTextRangeEnd) {
        this.displayTextRangeEnd = displayTextRangeEnd;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    public Status getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Status retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }

    public long[] getContributors() {
        return contributors;
    }

    public void setContributors(long[] contributors) {
        this.contributors = contributors;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isRetweetedByMe() {
        return isRetweetedByMe;
    }

    public void setRetweetedByMe(boolean retweetedByMe) {
        isRetweetedByMe = retweetedByMe;
    }

    public long getCurrentUserRetweetId() {
        return currentUserRetweetId;
    }

    public void setCurrentUserRetweetId(long currentUserRetweetId) {
        this.currentUserRetweetId = currentUserRetweetId;
    }

    public boolean isPossiblySensitive() {
        return isPossiblySensitive;
    }

    public void setPossiblySensitive(boolean possiblySensitive) {
        isPossiblySensitive = possiblySensitive;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Scopes getScopes() {
        return scopes;
    }

    public void setScopes(Scopes scopes) {
        this.scopes = scopes;
    }

    public String[] getWithheldInCountries() {
        return withheldInCountries;
    }

    public void setWithheldInCountries(String[] withheldInCountries) {
        this.withheldInCountries = withheldInCountries;
    }

    public long getQuotedStatusId() {
        return quotedStatusId;
    }

    public void setQuotedStatusId(long quotedStatusId) {
        this.quotedStatusId = quotedStatusId;
    }

    public Status getQuotedStatus() {
        return quotedStatus;
    }

    public void setQuotedStatus(Status quotedStatus) {
        this.quotedStatus = quotedStatus;
    }
}
