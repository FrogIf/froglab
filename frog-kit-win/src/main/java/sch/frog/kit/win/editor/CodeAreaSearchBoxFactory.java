package sch.frog.kit.win.editor;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import sch.frog.kit.win.MessageUtil;

import java.util.ArrayList;

public class CodeAreaSearchBoxFactory {

    public static SearchBox createSearchBox(BorderPane parentContainer, CodeArea codeArea){
        SearchAction searchAction = new SearchAction(codeArea);
        SearchBox searchBox = new SearchBox(parentContainer, (text, searchOverviewFetcher) -> {
            MessageUtil.clear();
            searchAction.search(text, true, searchOverviewFetcher);
        }, (text, searchOverviewFetcher) -> {
            MessageUtil.clear();
            searchAction.search(text, false, searchOverviewFetcher);
        });
        searchBox.onClose(codeArea::requestFocus);
        parentContainer.setOnKeyPressed(keyEvent -> {
            if(keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.F){
                parentContainer.setTop(searchBox);
                searchBox.focusSearch(codeArea.getSelectedText());
            }
        });
        codeArea.textProperty().addListener((observableValue, s, t1) -> searchAction.reset());
        return searchBox;
    }

    private static class SearchAction {

        private final CodeArea codeArea;

        public SearchAction(CodeArea codeArea) {
            this.codeArea = codeArea;
        }

        private ArrayList<SearchResult> searchResults = null;

        private String searchText;

        private boolean reachBottom;

        private boolean reachTop;

        private int lastCaretPos = -1;

        public void search(String searchText, boolean backward/*true -- 向后搜索, false - 向前搜索*/, SearchBox.SearchOverviewFetcher overviewFetcher){
            if(!searchText.equals(this.searchText)){ // 新的关键字
                this.searchText = searchText;
                this.searchResults = null;
                this.reachBottom = this.reachTop = false;
                buildSearchResult();
            }

            int cursorIndex = -1;
            if(this.searchResults.isEmpty()){
                MessageUtil.warn("no result found for : " + searchText);
            }else{
                int startIndex = backward ? searchResults.size() - 1 : 0;
                int start = codeArea.getCaretPosition();
                if(lastCaretPos != start){
                    lastCaretPos = start;
                    this.reachBottom = this.reachTop = false;
                }
                for(int i = 0, len = this.searchResults.size(); i < len; i++){
                    SearchResult result = searchResults.get(i);
                    int edge = backward ? result.start : (result.end + 1);
                    if(start < edge){
                        if(backward){
                            startIndex = i - 1;
                        }else{
                            startIndex = i;
                        }
                        break;
                    }
                }

                if(backward){
                    int nextIndex = startIndex + 1;
                    if(nextIndex == searchResults.size()){
                        if(!this.reachBottom){
                            cursorIndex = nextIndex - 1;
                            MessageUtil.warn("search reach bottom");
                        }else{
                            cursorIndex = 0;
                        }
                        this.reachBottom = !this.reachBottom;
                    }else{
                        cursorIndex = nextIndex;
                    }
                }else{
                    int nextIndex = startIndex - 1;
                    if(nextIndex == -1){
                        if(!this.reachTop){
                            cursorIndex = 0;
                            MessageUtil.warn("search reach top");
                        }else{
                            cursorIndex = searchResults.size() - 1;
                        }
                        this.reachTop = !this.reachTop;
                    }else{
                        cursorIndex = nextIndex;
                    }
                }

                if(cursorIndex >= 0){
                    focusFindResult(searchResults.get(cursorIndex));
                    overviewFetcher.setSearchOverview(new SearchBox.SearchOverview(searchResults.size() - 1, cursorIndex));
                }

            }
        }

        private void focusFindResult(SearchResult result){
            codeArea.selectRange(result.start, result.end);
            codeArea.requestFollowCaret();
        }

        private void buildSearchResult(){
            if(searchText == null || searchText.isEmpty()){
                searchResults = new ArrayList<>(0);
                return;
            }
            int len = searchText.length();
            String content = this.codeArea.getText();
            int index = content.indexOf(searchText);
            searchResults = new ArrayList<>();
            while(index >= 0){
                searchResults.add(new SearchResult(index, index + len));
                index = content.indexOf(searchText, index + len);
            }
        }

        public void reset(){
            this.searchText = null;
            this.searchResults = null;
            this.reachBottom = this.reachTop = false;
        }

    }

    private static class SearchResult {
        private final int start;
        private final int end;

        public SearchResult(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

}
