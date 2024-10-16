package one.tranic.mavenloader.common.updater.github.source;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubLatestReleaseSource {
    private String url;
    @SerializedName("assets_url")
    private String assetsUrl;
    @SerializedName("upload_url")
    private String uploadUrl;
    @SerializedName("html_url")
    private String htmlUrl;
    private long id;
    private Author author;
    @SerializedName("node_id")
    private String nodeId;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("target_commitish")
    private String targetCommitish;
    private String name;
    private boolean draft;
    private boolean prerelease;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("published_at")
    private String publishedAt;
    private List<Asset> assets;
    @SerializedName("tarball_url")
    private String tarballUrl;
    @SerializedName("zipball_url")
    private String zipballUrl;
    private String body;

    public GithubLatestReleaseSource() {
    }

    // Getters
    public String getUrl() {
        return url;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public long getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTargetCommitish() {
        return targetCommitish;
    }

    public String getName() {
        return name;
    }

    public boolean isDraft() {
        return draft;
    }

    public boolean isPrerelease() {
        return prerelease;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public String getTarballUrl() {
        return tarballUrl;
    }

    public String getZipballUrl() {
        return zipballUrl;
    }

    public String getBody() {
        return body;
    }

    // Nested classes
    public static class Author {
        private String login;
        private long id;
        @SerializedName("node_id")
        private String nodeId;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("gravatar_id")
        private String gravatarId;
        private String url;
        @SerializedName("html_url")
        private String htmlUrl;
        @SerializedName("followers_url")
        private String followersUrl;
        @SerializedName("following_url")
        private String followingUrl;
        @SerializedName("gists_url")
        private String gistsUrl;
        @SerializedName("starred_url")
        private String starredUrl;
        @SerializedName("subscriptions_url")
        private String subscriptionsUrl;
        @SerializedName("organizations_url")
        private String organizationsUrl;
        @SerializedName("repos_url")
        private String reposUrl;
        @SerializedName("events_url")
        private String eventsUrl;
        @SerializedName("received_events_url")
        private String receivedEventsUrl;
        private String type;
        @SerializedName("site_admin")
        private boolean siteAdmin;

        public Author() {
        }

        // Getters
        public String getLogin() {
            return login;
        }

        public long getId() {
            return id;
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public String getUrl() {
            return url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public String getFollowingUrl() {
            return followingUrl;
        }

        public String getGistsUrl() {
            return gistsUrl;
        }

        public String getStarredUrl() {
            return starredUrl;
        }

        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }

        public String getEventsUrl() {
            return eventsUrl;
        }

        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        public String getType() {
            return type;
        }

        public boolean isSiteAdmin() {
            return siteAdmin;
        }
    }

    public static class Asset {
        private String url;
        private long id;
        @SerializedName("node_id")
        private String nodeId;
        private String name;
        private String label;
        private Uploader uploader;
        @SerializedName("content_type")
        private String contentType;
        private String state;
        private long size;
        @SerializedName("download_count")
        private int downloadCount;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("browser_download_url")
        private String browserDownloadUrl;

        public Asset() {
        }

        // Getters
        public String getUrl() {
            return url;
        }

        public long getId() {
            return id;
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public Uploader getUploader() {
            return uploader;
        }

        public String getContentType() {
            return contentType;
        }

        public String getState() {
            return state;
        }

        public long getSize() {
            return size;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getBrowserDownloadUrl() {
            return browserDownloadUrl;
        }

        public static class Uploader {
            private String login;
            private long id;
            @SerializedName("node_id")
            private String nodeId;
            @SerializedName("avatar_url")
            private String avatarUrl;
            @SerializedName("gravatar_id")
            private String gravatarId;
            private String url;
            @SerializedName("html_url")
            private String htmlUrl;
            @SerializedName("followers_url")
            private String followersUrl;
            @SerializedName("following_url")
            private String followingUrl;
            @SerializedName("gists_url")
            private String gistsUrl;
            @SerializedName("starred_url")
            private String starredUrl;
            @SerializedName("subscriptions_url")
            private String subscriptionsUrl;
            @SerializedName("organizations_url")
            private String organizationsUrl;
            @SerializedName("repos_url")
            private String reposUrl;
            @SerializedName("events_url")
            private String eventsUrl;
            @SerializedName("received_events_url")
            private String receivedEventsUrl;
            private String type;
            @SerializedName("site_admin")
            private boolean siteAdmin;

            public Uploader() {
            }

            // Getters
            public String getLogin() {
                return login;
            }

            public long getId() {
                return id;
            }

            public String getNodeId() {
                return nodeId;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public String getGravatarId() {
                return gravatarId;
            }

            public String getUrl() {
                return url;
            }

            public String getHtmlUrl() {
                return htmlUrl;
            }

            public String getFollowersUrl() {
                return followersUrl;
            }

            public String getFollowingUrl() {
                return followingUrl;
            }

            public String getGistsUrl() {
                return gistsUrl;
            }

            public String getStarredUrl() {
                return starredUrl;
            }

            public String getSubscriptionsUrl() {
                return subscriptionsUrl;
            }

            public String getOrganizationsUrl() {
                return organizationsUrl;
            }

            public String getReposUrl() {
                return reposUrl;
            }

            public String getEventsUrl() {
                return eventsUrl;
            }

            public String getReceivedEventsUrl() {
                return receivedEventsUrl;
            }

            public String getType() {
                return type;
            }

            public boolean isSiteAdmin() {
                return siteAdmin;
            }
        }
    }
}
