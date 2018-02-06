# simple-android-flux
## 前言
    Flux:
    * 非形式化框架，更多的是一个思想。
    * 它是典型MVC/MVP框架的一个补充。
    * 学会使用Flux，需要将思路从命令式编程转向声明式编程。
    * 学会使用Flux，需要明确流的概念。
## 简介
    Flux是Facebook用户建立客户端Web应用的前端架构，它通过利用一个单向的数据流补充了React的组合视图组件，这更是一种模式
    而非正式框架，你能够无需许多新代码情况下立即开始使用Flux。
    Flux应用有三个主要部分：Dispatcher调度 、存储Store和视图View(React 组件)，这些不应该和
    MVC:Model-View-Controll(模型-视图-控制器)混淆，控制器在Flux应用中是存在的，但是他们是controller-view(控制器-视图)，
    视图通常在一个结构顶部，而这种结构是用来从存储stroe获得数据，然后将数据传递到自己的子结构们，此外，Action创建者
    -Dispatcher的帮助类的方法-用于支持一个语义API，这个API是描述应用程序中所有变化的可能，通常可将它们看成是Flux
    更新循环的第四部分。
    Flux是以单向数据流方式支持MVC，当一个用户和React视图交互时，视图会将这个动作传播到一个中央Dispatcher，一直到各种存储，
    在那里保存着应用的数据和业务逻辑，这个使用React的声明式风格的过程是非常棒的，能够允许存储发送更新信息，而无需指定在状态
    之间如何切换视图。(传统方式更新状态后，会推出一个新的视图页面。)
## 流程图
![](https://github.com/ZLOVE320483/simple-android-flux/blob/master/img/flux1.jpg)
>
    介绍一下流程图中的各个成员：
* Dispatcher：整个流程中唯一枢纽，负责发送和分发事件流，以及管理各个Store之间的关系。
* Store：仓库，对应MVP中的P，负责获取M并根据业务逻辑，重组M的源数据，并通知V进行视图的渲染。
* View（其实应该叫做Control-View）：对应MVP中的V，对应Android中的Activity和Fragment。负责夹在xml，实现控件事件的触发，最重要的是负责接收Store中的业务数据，进行试图渲染。
* Action：一个事件，往往有明确含义，大部分场景下，其内容等同于Model。
* ActionCreator：产生Action的模块，其意义就是将原MVP中，V层控件触发的业务逻辑封装起来。
* Event：子事件，往往有明确含义，用于Store与Control-View间的通信。
>
    有两种形式的Event：1.推Push：携带经由Store转换过后的新数据，交由Control-View，进行视图渲染；
    2.拉Pull：仅发送空事件，Control-View接收后，主动去Store中获取。
## 具体实例流程图展示：
![](https://github.com/ZLOVE320483/simple-android-flux/blob/master/img/flux2.jpg)
>
    以服务器交互举例，事件流的具体过程：
    1.从服务器异步获取数据（也可以是一些数据库操作，本地文件读取等）；
    2.通过ActionCreator产生某个有明确意义的Action；
    3.将该Action发送至事件总线的分发器；
    4.分发器将Action发送至声明接收该Action的Store；
    5.Store拆解收到Action后，直接发送Event或在接收若干个Action，集合所有事件后触发发送某个Event；
    6.将该Event发送至事件总线的分发器；
    7.分发器将Event发送至生接收该Action的ControlView；
    8.ControlView所渲染的页面上，触发一个用户行为，通知ActionCreator。
## Android中的Flux实例演示
![](https://github.com/ZLOVE320483/simple-android-flux/blob/master/img/flux3.jpg)
>
    这个例子实现的功能是一个简易的参加唱歌比赛的歌手信息录入系统。
Dispatcher: 负责事件的分发
>
    private final Bus bus;
    private static Dispatcher instance;

    public static Dispatcher get(Bus bus) {
        if (instance == null) {
            instance = new Dispatcher(bus);
        }
        return instance;
    }

    Dispatcher(Bus bus) {
        this.bus = bus;
    }

    public void register(final Object cls) {
        bus.register(cls);
    }

    public void unregister(final Object cls) {
        bus.unregister(cls);
    }

    public void emitChange(Store.StoreChangeEvent o) {
        post(o);
    }

    public void dispatch(String type, Object... data) {
        if (isEmpty(type)) {
            throw new IllegalArgumentException("Type must not be empty");
        }

        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
        }

        Action.Builder actionBuilder = Action.type(type);
        int i = 0;
        while (i < data.length) {
            String key = (String) data[i++];
            Object value = data[i++];
            actionBuilder.bundle(key, value);
        }
        post(actionBuilder.build());
    }

    private boolean isEmpty(String type) {
        return type == null || type.isEmpty();
    }

    private void post(final Object event) {
        bus.post(event);
    }
SingerStore:负责数据存储以及数据更新后发送更新事件
>
    private static SingerStore instance;
    private final List<Singer> singers;

    public SingerStore(Dispatcher dispatcher) {
        super(dispatcher);
        singers = new ArrayList<>();
    }

    public static SingerStore get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new SingerStore(dispatcher);
        }
        return instance;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    @Override
    @Subscribe
    public void onAction(Action action) {
        switch (action.getType()) {
            case SingerActions.ActionType.CREATE_ACTION:
                Singer singer =((Singer) action.getData().get(SingerActions.EventObjectKey.KEY_SINGER));
                addElement(singer);
                emitStoreChange();
                break;
            case SingerActions.ActionType.DELETE_ACTION:
                long id = ((long) action.getData().get(SingerActions.EventObjectKey.KEY_SINGER_ID));
                deleteElement(id);
                emitStoreChange();
                break;
        }
    }


    private void addElement(Singer clone) {
        singers.add(clone);
        Collections.sort(singers);
    }

    private void deleteElement(long id) {
        Iterator<Singer> iterator = singers.iterator();
        while (iterator.hasNext()) {
            Singer singer = iterator.next();
            if (singer.getId() == id) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    StoreChangeEvent changeEvent() {
        return new SingerStoreChangeEvent();
    }

    public class SingerStoreChangeEvent implements StoreChangeEvent {
    }
ActionsCreator:负责产生各类事件
>
    private static ActionsCreator instance;
    final Dispatcher dispatcher;

    ActionsCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static ActionsCreator get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new ActionsCreator(dispatcher);
        }
        return instance;
    }

    public void create(Singer singer) {
        dispatcher.dispatch(SingerActions.ActionType.CREATE_ACTION,
                SingerActions.EventObjectKey.KEY_SINGER,
                singer);
    }

    public void delete(long id) {
        dispatcher.dispatch(SingerActions.ActionType.DELETE_ACTION,
                SingerActions.EventObjectKey.KEY_SINGER_ID,
                id);
    }
## 总结
    1.Flux与其说是框架，不如说是一种思路。
    2.继承MVP框架模式，将M与V隔离。额外的，其通过声明式编程，将P与V进一步解耦。
    3.由于是单向的，不存在逆向调用，使整体代码流程清晰。
    4.一般通过事件总线来实现。
>
[参考文章](http://www.jdon.com/idea/flux.html)
>
[其他介绍文章](https://www.zhihu.com/question/33864532/answer/57667838)
