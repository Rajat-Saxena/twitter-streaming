package com.serializer;

import twitter4j.*;

import java.util.Date;

class CustomStatus {
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

    HashtagEntity[] hashtagEntities;


    MediaEntity[] mediaEntities;


    SymbolEntity[] symbolEntities;

    public UserMentionEntity[] getUserMentionEntities() {
        return userMentionEntities;
    }

    public void setUserMentionEntities(UserMentionEntity[] userMentionEntities) {
        this.userMentionEntities = userMentionEntities;
    }

    int getAccessLevel() {
        return accessLevel;
    }

    void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    void setRateLimitStatus(RateLimitStatus rateLimitStatus) {
        this.rateLimitStatus = rateLimitStatus;
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    Date getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    int getDisplayTextRangeStart() {
        return displayTextRangeStart;
    }

    void setDisplayTextRangeStart(int displayTextRangeStart) {
        this.displayTextRangeStart = displayTextRangeStart;
    }

    int getDisplayTextRangeEnd() {
        return displayTextRangeEnd;
    }

    void setDisplayTextRangeEnd(int displayTextRangeEnd) {
        this.displayTextRangeEnd = displayTextRangeEnd;
    }

    String getSource() {
        return source;
    }

    void setSource(String source) {
        this.source = source;
    }

    boolean isTruncated() {
        return isTruncated;
    }

    void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    long getInReplyToUserId() {
        return inReplyToUserId;
    }

    void setInReplyToUserId(long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    GeoLocation getGeoLocation() {
        return geoLocation;
    }

    void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    Place getPlace() {
        return place;
    }

    void setPlace(Place place) {
        this.place = place;
    }

    boolean isFavorited() {
        return isFavorited;
    }

    void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    boolean isRetweeted() {
        return isRetweeted;
    }

    void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    int getFavoriteCount() {
        return favoriteCount;
    }

    void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    boolean isRetweet() {
        return isRetweet;
    }

    void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    Status getRetweetedStatus() {
        return retweetedStatus;
    }

    void setRetweetedStatus(Status retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }

    long[] getContributors() {
        return contributors;
    }

    void setContributors(long[] contributors) {
        this.contributors = contributors;
    }

    int getRetweetCount() {
        return retweetCount;
    }

    void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    boolean isRetweetedByMe() {
        return isRetweetedByMe;
    }

    void setRetweetedByMe(boolean retweetedByMe) {
        isRetweetedByMe = retweetedByMe;
    }

    long getCurrentUserRetweetId() {
        return currentUserRetweetId;
    }

    void setCurrentUserRetweetId(long currentUserRetweetId) {
        this.currentUserRetweetId = currentUserRetweetId;
    }

    boolean isPossiblySensitive() {
        return isPossiblySensitive;
    }

    void setPossiblySensitive(boolean possiblySensitive) {
        isPossiblySensitive = possiblySensitive;
    }

    String getLang() {
        return lang;
    }

    void setLang(String lang) {
        this.lang = lang;
    }

    Scopes getScopes() {
        return scopes;
    }

    void setScopes(Scopes scopes) {
        this.scopes = scopes;
    }

    String[] getWithheldInCountries() {
        return withheldInCountries;
    }

    void setWithheldInCountries(String[] withheldInCountries) {
        this.withheldInCountries = withheldInCountries;
    }

    long getQuotedStatusId() {
        return quotedStatusId;
    }

    void setQuotedStatusId(long quotedStatusId) {
        this.quotedStatusId = quotedStatusId;
    }

    Status getQuotedStatus() {
        return quotedStatus;
    }

    void setQuotedStatus(Status quotedStatus) {
        this.quotedStatus = quotedStatus;
    }
}
