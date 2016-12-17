package com.app.comic.ui.Model.Receive.Comic;

import com.app.comic.ui.Model.Receive.TBD.LoginReceive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 10/9/2016.
 */

public class ComicReceive {

    private String status;
    // private String message;
    private ComicList data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   /* public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/

    public ComicList getData() {
        return data;
    }

    public void setData(ComicList data) {
        this.data = data;
    }

    public class ComicList {

        List<NextLevelOptions> next_level_options;
        List<NextLevelOptions> pages;
        private String previous_level;
        private String previous_option;


        public String getPrevious_level() {
            return previous_level;
        }

        public void setPrevious_level(String previous_level) {
            this.previous_level = previous_level;
        }

        public String getPrevious_option() {
            return previous_option;
        }

        public void setPrevious_option(String previous_option) {
            this.previous_option = previous_option;
        }

        public List<NextLevelOptions> getPages() {
            return pages;
        }

        public void setPages(List<NextLevelOptions> pages) {
            this.pages = pages;
        }

        public List<NextLevelOptions> getNext_level_options() {
            return next_level_options;
        }

        public void setNext_level_options(List<NextLevelOptions> next_level_options) {
            this.next_level_options = next_level_options;
        }

        public class NextLevelOptions {

            private String id;
            private String page_id;
            private String character;
            private String level;
            private String option;
            private String page;
            private String image_name;
            private String is_sharable;
            private String sharable_image;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPage_id() {
                return page_id;
            }

            public void setPage_id(String page_id) {
                this.page_id = page_id;
            }

            public String getCharacter() {
                return character;
            }

            public void setCharacter(String character) {
                this.character = character;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getOption() {
                return option;
            }

            public void setOption(String option) {
                this.option = option;
            }

            public String getPage() {
                return page;
            }

            public void setPage(String page) {
                this.page = page;
            }

            public String getImage_name() {
                return image_name;
            }

            public void setImage_name(String image_name) {
                this.image_name = image_name;
            }

            public String getIs_shareable() {
                return is_sharable;
            }

            public void setIs_shareable(String is_shareable) {
                this.is_sharable = is_shareable;
            }

            public String getShareable_image() {
                return sharable_image;
            }

            public void setShareable_image(String shareable_image) {
                this.sharable_image = shareable_image;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }


        }
    }


    public ComicReceive(ComicReceive data) {

        this.data = data.getData();
        this.status = data.getStatus();
        //  this.message = data.getMessage();

    }


}
