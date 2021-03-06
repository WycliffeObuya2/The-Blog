package com.yourstar.cenotomy.Activities.Fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;
import com.yourstar.cenotomy.Activities.Startup;
import com.yourstar.cenotomy.Constants;
import com.youstar.bloggerssport.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArticlesViewModel extends ViewModel {

    private String [] urlString = Startup.getResource().getStringArray(R.array.links);
    private MutableLiveData<List<Article>> articlelist;
    public LiveData<List<Article>> getArticles() {
        if (articlelist == null) {
            articlelist = new MutableLiveData<List<Article>>();
            loadUsers();
        }
        return articlelist;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch articles
        final List<Article> templist = new ArrayList<>();

        for(String ul:urlString){
            Parser parser = new Parser();
            parser.execute(ul);
            parser.onFinish(new Parser.OnTaskCompleted() {
               @Override
               public void onTaskCompleted(ArrayList<Article> arrayList) {

                   templist.addAll(arrayList);

                   Collections.sort(templist, new Comparator<Article>() {
                       @Override
                       public int compare(Article v1, Article v2) {
                           return v1.getPubDate().compareTo(v2.getPubDate());
                       }

                   });
                   Collections.reverse(templist);

                   articlelist.postValue(templist);
               }

               @Override
               public void onError() {
                   EventBus.getDefault().post(Constants.NotLoaded);
               }
           });


        }

        Log.e("Size is ", String.valueOf(articlelist.getValue()));
    }
    public  void Clear(){
        articlelist = null;
    }
}
