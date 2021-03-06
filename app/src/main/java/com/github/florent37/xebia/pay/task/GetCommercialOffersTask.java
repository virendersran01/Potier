package com.github.florent37.xebia.pay.task;

import com.github.florent37.xebia.model.Book;
import com.github.florent37.xebia.model.Offer;
import com.github.florent37.xebia.model.OfferContainer;
import com.github.florent37.xebia.webservice.HenriPotierWebService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by florentchampigny on 27/04/15.
 * A task to simplify webservice call
 */
public class GetCommercialOffersTask {

    //callback to implement on caller
    public interface GetCommercialOfferTaskCallBack{
        public void onCommercialOfferReceived(List<Offer> offers);
        public void onNetworkError();
    }

    private GetCommercialOfferTaskCallBack callBack;

    public GetCommercialOffersTask(GetCommercialOfferTaskCallBack callBack){
        this.callBack = callBack;
    }

    /**
     * Create ISBNS string with , betweet
     * @return
     */
    private String getIsbnsFromBooks(List<Book> books){
        StringBuilder stringBuilder = new StringBuilder();
        int size = books.size();
        for(int i=0;i<size;++i){
            stringBuilder.append(books.get(i).getIsbn());
            if(i<size-1)
                stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    public void execute(List<Book> books){
        if(books != null && !books.isEmpty()) {

            String isbns = getIsbnsFromBooks(books);

            //use retrofit service
            HenriPotierWebService.getInstance().getCommercialOffers(isbns,new Callback<OfferContainer>() {
                @Override
                public void success(OfferContainer offerContainer, Response response) {
                    if (callBack != null && offerContainer != null && offerContainer.getOffers() != null)
                        callBack.onCommercialOfferReceived(offerContainer.getOffers());
                }

                @Override
                public void failure(RetrofitError error) {
                    if (callBack != null)
                        callBack.onNetworkError();
                }
            });
        }
    }

}
