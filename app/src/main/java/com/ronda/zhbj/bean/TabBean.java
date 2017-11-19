package com.ronda.zhbj.bean;

import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/05
 * Version: v1.0
 */

public class TabBean {

    /**
     * countcommenturl : http://zhbj.qianlong.com/client/content/countComment/
     * more : /10007/list_2.json
     * news : [{"comment":true,"commentlist":"http://10.0.2.2:8080/zhbj/10007/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/35319","id":35311,"listimage":"http://10.0.2.2:8080/zhbj/10007/2078369924F9UO.jpg","pubdate":"2014-10-1113:18","title":"网上大讲堂第368期预告：义务环保人人有责","type":"news","url":"http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html"}]
     * title : 北京
     * topic : [{"description":"11111111","id":10101,"listimage":"http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg","sort":1,"title":"北京","url":"http://10.0.2.2:8080/zhbj/10007/list_1.json"}]
     * topnews : [{"comment":true,"commentlist":"http://10.0.2.2:8080/zhbj/10007/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/35301","id":35301,"pubdate":"2014-04-0814:24","title":"蜗居生活","topimage":"http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg","type":"news","url":"http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html"}]
     */

    private DataBean data;
    /**
     * data : {"countcommenturl":"http://zhbj.qianlong.com/client/content/countComment/","more":"/10007/list_2.json","news":[{"comment":true,"commentlist":"http://10.0.2.2:8080/zhbj/10007/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/35319","id":35311,"listimage":"http://10.0.2.2:8080/zhbj/10007/2078369924F9UO.jpg","pubdate":"2014-10-1113:18","title":"网上大讲堂第368期预告：义务环保人人有责","type":"news","url":"http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html"}],"title":"北京","topic":[{"description":"11111111","id":10101,"listimage":"http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg","sort":1,"title":"北京","url":"http://10.0.2.2:8080/zhbj/10007/list_1.json"}],"topnews":[{"comment":true,"commentlist":"http://10.0.2.2:8080/zhbj/10007/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/35301","id":35301,"pubdate":"2014-04-0814:24","title":"蜗居生活","topimage":"http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg","type":"news","url":"http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html"}]}
     * retcode : 200
     */

    private int retcode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public static class DataBean {
        private String countcommenturl;
        private String more;
        private String title;
        /**
         * comment : true
         * commentlist : http://10.0.2.2:8080/zhbj/10007/comment_1.json
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/35319
         * id : 35311
         * listimage : http://10.0.2.2:8080/zhbj/10007/2078369924F9UO.jpg
         * pubdate : 2014-10-1113:18
         * title : 网上大讲堂第368期预告：义务环保人人有责
         * type : news
         * url : http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html
         */

        private List<NewsBean> news;
        /**
         * description : 11111111
         * id : 10101
         * listimage : http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg
         * sort : 1
         * title : 北京
         * url : http://10.0.2.2:8080/zhbj/10007/list_1.json
         */

        private List<TopicBean> topic;
        /**
         * comment : true
         * commentlist : http://10.0.2.2:8080/zhbj/10007/comment_1.json
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/35301
         * id : 35301
         * pubdate : 2014-04-0814:24
         * title : 蜗居生活
         * topimage : http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg
         * type : news
         * url : http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html
         */

        private List<TopnewsBean> topnews;

        public String getCountcommenturl() {
            return countcommenturl;
        }

        public void setCountcommenturl(String countcommenturl) {
            this.countcommenturl = countcommenturl;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<NewsBean> getNews() {
            return news;
        }

        public void setNews(List<NewsBean> news) {
            this.news = news;
        }

        public List<TopicBean> getTopic() {
            return topic;
        }

        public void setTopic(List<TopicBean> topic) {
            this.topic = topic;
        }

        public List<TopnewsBean> getTopnews() {
            return topnews;
        }

        public void setTopnews(List<TopnewsBean> topnews) {
            this.topnews = topnews;
        }

        public static class NewsBean {
            private boolean comment;
            private String commentlist;
            private String commenturl;
            private int id;
            private String listimage;
            private String pubdate;
            private String title;
            private String type;
            private String url;

            public boolean isComment() {
                return comment;
            }

            public void setComment(boolean comment) {
                this.comment = comment;
            }

            public String getCommentlist() {
                return commentlist;
            }

            public void setCommentlist(String commentlist) {
                this.commentlist = commentlist;
            }

            public String getCommenturl() {
                return commenturl;
            }

            public void setCommenturl(String commenturl) {
                this.commenturl = commenturl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getListimage() {
                return listimage;
            }

            public void setListimage(String listimage) {
                this.listimage = listimage;
            }

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class TopicBean {
            private String description;
            private int id;
            private String listimage;
            private int sort;
            private String title;
            private String url;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getListimage() {
                return listimage;
            }

            public void setListimage(String listimage) {
                this.listimage = listimage;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class TopnewsBean {
            private boolean comment;
            private String commentlist;
            private String commenturl;
            private int id;
            private String pubdate;
            private String title;
            private String topimage;
            private String type;
            private String url;

            public boolean isComment() {
                return comment;
            }

            public void setComment(boolean comment) {
                this.comment = comment;
            }

            public String getCommentlist() {
                return commentlist;
            }

            public void setCommentlist(String commentlist) {
                this.commentlist = commentlist;
            }

            public String getCommenturl() {
                return commenturl;
            }

            public void setCommenturl(String commenturl) {
                this.commenturl = commenturl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTopimage() {
                return topimage;
            }

            public void setTopimage(String topimage) {
                this.topimage = topimage;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
