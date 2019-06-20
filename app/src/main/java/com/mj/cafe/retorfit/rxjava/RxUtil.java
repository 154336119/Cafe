package com.mj.cafe.retorfit.rxjava;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：
 * Created by Lee
 * on 2016/9/19.
 */
public class RxUtil {
    /**
     * retrofi工作和UI线程切换的封装
     * @param <T>
     * @return
     */
    public static <T>Observable.Transformer<T,T> applySchedulersForRetrofit(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }
    public static <T>Observable.Transformer<T,T> applyHttpEntityForRetrofit(){
         return new Observable.Transformer<T, T>() {
             @Override
             public Observable<T> call(Observable<T> tObservable) {
                 return tObservable.onErrorResumeNext(new HttpResponseFunc<T>());
             }
         };
    }


//    public static <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull final ActivityEvent event, final Observable<ActivityEvent> lifecycleSubject) {
//        return new Observable.Transformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> sourceObservable) {
//                Observable<ActivityEvent> compareLifecycleObservable =
//                        lifecycleSubject.takeFirst(new Func1<ActivityEvent, Boolean>() {
//                            @Override
//                            public Boolean call(ActivityEvent activityLifeCycleEvent) {
//                                return activityLifeCycleEvent.equals(event);
//                            }
//                        });
//                return sourceObservable.takeUntil(compareLifecycleObservable);
//            }
//        };
//    }
//    public static <T> Observable.Transformer<T, T> bindUntilFragmentEvent(@NonNull final FragmentEvent event, final Observable<FragmentEvent> lifecycleSubject) {
//        return new Observable.Transformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> sourceObservable) {
//                Observable<FragmentEvent> compareLifecycleObservable =
//                        lifecycleSubject.takeFirst(new Func1<FragmentEvent, Boolean>() {
//                            @Override
//                            public Boolean call(FragmentEvent activityLifeCycleEvent) {
//                                return activityLifeCycleEvent.equals(event);
//                            }
//                        });
//                return sourceObservable.takeUntil(compareLifecycleObservable);
//            }
//        };
//    }

}
