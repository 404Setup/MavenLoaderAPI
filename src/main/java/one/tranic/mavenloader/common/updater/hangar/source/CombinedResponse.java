package one.tranic.mavenloader.common.updater.hangar.source;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CombinedResponse {
    private Pagination pagination;
    private List<VersionResult> result;

    public CombinedResponse() {
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<VersionResult> getResult() {
        return result;
    }

    public static class Pagination {
        private int limit;
        private int offset;
        private int count;

        public Pagination() {
        }

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }

        public int getCount() {
            return count;
        }
    }

    public static class VersionResult {
        private String createdAt;
        private String name;
        private String visibility;
        private String description;
        private Stats stats;
        private String author;
        private String reviewState;
        private Channel channel;
        private String pinnedStatus;
        private Map<String, DownloadInfo> downloads;
        private Map<String, List<PluginDependency>> pluginDependencies;
        private Map<String, List<String>> platformDependencies;
        private Map<String, List<String>> platformDependenciesFormatted;

        public VersionResult() {
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getName() {
            return name;
        }

        public String getVisibility() {
            return visibility;
        }

        public String getDescription() {
            return description;
        }

        public Stats getStats() {
            return stats;
        }

        public String getAuthor() {
            return author;
        }

        public String getReviewState() {
            return reviewState;
        }

        public Channel getChannel() {
            return channel;
        }

        public String getPinnedStatus() {
            return pinnedStatus;
        }

        public Map<String, DownloadInfo> getDownloads() {
            return downloads;
        }

        public Map<String, List<PluginDependency>> getPluginDependencies() {
            return pluginDependencies;
        }

        public Map<String, List<String>> getPlatformDependencies() {
            return platformDependencies;
        }

        public Map<String, List<String>> getPlatformDependenciesFormatted() {
            return platformDependenciesFormatted;
        }

        public static class Stats {
            private int totalDownloads;
            private Map<String, Integer> platformDownloads;

            public Stats() {
            }

            public int getTotalDownloads() {
                return totalDownloads;
            }

            public Map<String, Integer> getPlatformDownloads() {
                return platformDownloads;
            }
        }

        public static class Channel {
            private String name;
            private String createdAt;
            private String description;
            private String color;
            private List<String> flags;

            public Channel() {
            }

            public String getName() {
                return name;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public String getDescription() {
                return description;
            }

            public String getColor() {
                return color;
            }

            public List<String> getFlags() {
                return flags;
            }
        }

        public static class FileInfo {
            private String name;
            private int sizeBytes;
            private String sha256Hash;

            public FileInfo() {
            }

            public String getName() {
                return name;
            }

            public int getSizeBytes() {
                return sizeBytes;
            }

            public String getSha256Hash() {
                return sha256Hash;
            }
        }

        public static class DownloadInfo {
            private FileInfo fileInfo;
            private String externalUrl;
            private String downloadUrl;

            public DownloadInfo() {
            }

            public FileInfo getFileInfo() {
                return fileInfo;
            }

            public @Nullable String getExternalUrl() {
                return externalUrl;
            }

            public String getDownloadUrl() {
                return downloadUrl;
            }
        }

        public static class PluginDependency {
            private String name;
            private boolean required;
            private String externalUrl;
            private String platform;

            public PluginDependency() {
            }

            public String getName() {
                return name;
            }

            public boolean isRequired() {
                return required;
            }

            public @Nullable String getExternalUrl() {
                return externalUrl;
            }

            public String getPlatform() {
                return platform;
            }
        }
    }
}