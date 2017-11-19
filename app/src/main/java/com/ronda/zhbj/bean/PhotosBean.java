package com.ronda.zhbj.bean;

import java.util.List;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/11/17
 * Version: v1.0
 */

public class PhotosBean {
    /**
     * countcommenturl : http://zhbj.qianlong.com/client/content/countComment/
     * more : http://zhbj.qianlong.com/static/api/news/10003/list_2.json
     * news : [{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/82772","id":82772,"largeimage":"http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg","pubdate":"2014-11-07 11:40","smallimage":"http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg","title":"北京·APEC绚丽之夜","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html"}]
     * title : 组图
     * topic : []
     */

    private DataBean data;
    /**
     * data : {"countcommenturl":"http://zhbj.qianlong.com/client/content/countComment/","more":"http://zhbj.qianlong.com/static/api/news/10003/list_2.json","news":[{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/82772","id":82772,"largeimage":"http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg","pubdate":"2014-11-07 11:40","smallimage":"http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg","title":"北京·APEC绚丽之夜","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html"}],"title":"组图","topic":[]}
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
         * commentlist : http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/82772
         * id : 82772
         * largeimage : http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg
         * listimage : http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg
         * pubdate : 2014-11-07 11:40
         * smallimage : http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg
         * title : 北京·APEC绚丽之夜
         * type : news
         * url : http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html
         */

        private List<NewsBean> news;
        private List<?> topic;

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

        public List<?> getTopic() {
            return topic;
        }

        public void setTopic(List<?> topic) {
            this.topic = topic;
        }

        public static class NewsBean {
            private boolean comment;
            private String commentlist;
            private String commenturl;
            private int id;
            private String largeimage;
            private String listimage;
            private String pubdate;
            private String smallimage;
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

            public String getLargeimage() {
                return largeimage;
            }

            public void setLargeimage(String largeimage) {
                this.largeimage = largeimage;
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

            public String getSmallimage() {
                return smallimage;
            }

            public void setSmallimage(String smallimage) {
                this.smallimage = smallimage;
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
    }
}
