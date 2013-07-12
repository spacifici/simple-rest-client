# Simple Rest Client

## Introduction

__Simple Rest Client__ is an experiment about a simple rest client with caching support. By using reflection and a
proxy, it allows the user to use a json rest service by simply writing a java interface. It only supports json as data
exchange format (by using [Gson](https://code.google.com/p/google-gson/)), and uses
[OkHttp](https://github.com/square/okhttp) as HTTP client. You can use it also on Android.

## How to use it

1. Declare a java interface 
  
  ```java
  @RestService("http://www.mybookstore.example.com/rest")
  public interface BookStoreService {    
      @GET
      @Path("books/{ISBN}")
      @Cache
      Book getBook(@PathParam("ISBN") String ISBN);
      
      @GET
      @Path("books/{ISBN}/reviews")
      ReviewSummary[] getRewiewsSummary(@PathParam("ISBN") String ISBN);
      
      @GET
      @Path("books/{ISBN}/reviews")
      ReviewSummary[] getRewiewsSummary(@PathParam("ISBN") String ISBN,
                                        @QueryParam("from") int from,
                                        @QueryParam("to") int to);
      
      @GET
      @Path("books/{ISBN}/reviews/{ID}")
      Review getReview(@PathParam("ISBN") String ISBN,
                       @PathParam("ID") String reviewID);
      
      @POST
      @Path("books/{ISBN}/reviews/create")
      Review postReview(@HeaderParam("Auth-Token") String token,
                        @PathParam("ISBN") String ISBN,
                        Review review);
  }
  ```

2. Use the ```RestClientFactory``` to instantiate the client

  ```java
  BookStoreService service = RestClientFactory.createClient(BookStoreService.class);
  ```

3. Call the service methods

  ```java
    Book book = service.getBook("978-0123745149");
    ...
    ReviewSummary[] summaries = service.getReviewsSummary("978-0321563842", 10, 20);
    ...
    Review review = service.getReview("978-0321349606", "d52387880");
    ...
    Review myReview = ...
    Review result = service.postReview(token, "978-0201633610", myReview);
    if (result != null) {
      ...
    }
  ```

4. Eventually change client properties
  
  ```java
    RestClientInterface restClient = (RestClientInterface) service;
    restClient.setBaseUrl("http://myotherbookstore.example.com/services");
  ```

## TODO

1. Callback mechanism for exceptions thrown inside the service implementation
2. Callback mechanism to handle HTTP responses not equals to 200
3. OAuth support
4. Add xml data exchange format
5. Add compile time class generation to prevent the use of a proxy and the massive reflection usage _(it sounds funny,
probably it will go first)_
