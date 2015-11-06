/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.test.hasor.core._01_bean;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Hasor;
import net.hasor.core.Module;
import net.test.hasor.core._01_bean.pojo.PojoBean;
import net.test.hasor.core._01_bean.pojo.PojoBeanFactory;
import net.test.hasor.core._01_bean.pojo.PojoInfo;
import org.junit.Test;
import org.more.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 1.beanTest
 *      基本的Bean用法。
 * 2.interfaceBindTest
 *      给一个接口指定一个实现类。
 * 3.nameBindTest
 *      用名字区分相同类型的两个不同Bean。
 * 4.singletonBeanTest
 *      单例模式。
 * 5.customBeanTest
 *      托管一个自己创建的Bean，被托管的Bean将成为单例。
 * 6.idBeanTest
 *      为Bean起一个唯一的名字，然后通过名字获取它。
 * 7.factoryBeanTest
 *      工厂方式创建Bean。
 * 
 * @version : 2015年11月6日
 * @author 赵永春(zyc@hasor.net)
 */
public class BeanTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    //
    /* Bean */
    @Test
    public void beanTest() {
        System.out.println("--->>beanTest<<--");
        AppContext appContext = Hasor.createAppContext();
        //
        PojoBean myBean = appContext.getInstance(PojoBean.class);
        //
        logger.debug(JSON.toString(myBean));
    }
    //
    /* 为一个类型指定一个实现类。 */
    @Test
    public void interfaceBindTest() {
        System.out.println("--->>interfaceBindTest<<--");
        AppContext appContext = Hasor.createAppContext(new Module() {
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                /*为一个类型指定一个实现类*/
                apiBinder.bindType(PojoInfo.class).to(PojoBean.class);
            }
        });
        //
        //通过类型获取实现类实例。
        PojoInfo myBean2 = appContext.getInstance(PojoInfo.class);
        logger.debug(JSON.toString(myBean2));
    }
    //
    /* 根据名字区分同一个类型的两个Bean。 */
    @Test
    public void nameBindTest() {
        System.out.println("--->>nameBindTest<<--");
        AppContext appContext = Hasor.createAppContext(new Module() {
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                //根据名字区分同一个类型的两个Bean。
                // - 例子中通过为不同的Bean注入不同的值用来区分它们。
                apiBinder.bindType(PojoInfo.class).nameWith("UserA").to(PojoBean.class).injectValue("name", "马A");
                apiBinder.bindType(PojoInfo.class).nameWith("UserB").to(PojoBean.class).injectValue("name", "小六");
            }
        });
        //
        PojoInfo userA = appContext.findBindingBean("UserA", PojoInfo.class);
        PojoInfo userB = appContext.findBindingBean("UserB", PojoInfo.class);
        //
        logger.debug("userA :" + JSON.toString(userA));
        logger.debug("userB :" + JSON.toString(userB));
    }
    //
    /* 单例模式 */
    @Test
    public void singletonBeanTest() {
        System.out.println("--->>singletonBeanTest<<--");
        AppContext appContext = Hasor.createAppContext(new Module() {
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                apiBinder.bindType(PojoBean.class).asEagerSingleton();
            }
        });
        //
        PojoInfo objectA = appContext.getInstance(PojoBean.class);
        PojoInfo objectB = appContext.getInstance(PojoBean.class);
        //
        logger.debug("objectBody :" + JSON.toString(objectA));
        logger.debug("objectA eq objectB = " + (objectA == objectB));
    }
    //
    /* 托管一个自己创建的Bean，被托管的Bean将成为单例。 */
    @Test
    public void customBeanTest() {
        System.out.println("--->>customBeanTest<<--");
        AppContext appContext = Hasor.createAppContext(new Module() {
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                PojoBean pojo = new PojoBean();
                pojo.setName("马大帅");
                apiBinder.bindType(PojoBean.class).toInstance(pojo);
            }
        });
        //
        PojoInfo objectA = appContext.getInstance(PojoBean.class);
        PojoInfo objectB = appContext.getInstance(PojoBean.class);
        //
        logger.debug("objectBody :" + JSON.toString(objectA));
        logger.debug("objectA eq objectB = " + (objectA == objectB));
    }
    //
    /* 为Bean起一个唯一的名字，然后通过名字获取它。  */
    @Test
    public void idBindBeanTest() {
        System.out.println("--->>idBindBeanTest<<--");
        //1.创建一个标准的 Hasor 容器。
        AppContext appContext = Hasor.createAppContext(new Module() {
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                //Bean1
                apiBinder.bindType(PojoBean.class).idWith("myBean1");
                //Bean2
                PojoBean pojo = new PojoBean();
                pojo.setName("刘三姐");
                apiBinder.bindType(PojoBean.class).idWith("myBean2").toInstance(pojo);
            }
        });
        //
        PojoBean myBean1 = appContext.getInstance("myBean1");
        PojoBean myBean2 = appContext.getInstance("myBean2");
        logger.debug("myBean1 :" + JSON.toString(myBean1));
        logger.debug("myBean2 :" + JSON.toString(myBean2));
    }
    //
    /* 工厂方式创建Bean  */
    @Test
    public void factoryBeanTest() {
        System.out.println("--->>factoryBeanTest<<--");
        AppContext appContext = Hasor.createAppContext(new Module() {
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                apiBinder.bindType(PojoInfo.class).toProvider(new PojoBeanFactory());
            }
        });
        //
        PojoInfo myBean = appContext.getInstance(PojoInfo.class);
        logger.debug(JSON.toString(myBean));
    }
}