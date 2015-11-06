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
package net.test.hasor.core._02_ioc.pojo;
import net.test.hasor.core._01_bean.pojo.PojoInfo;
/**
 * 一个Bean
 * @version : 2014-1-3
 * @author 赵永春(zyc@hasor.net)
 */
public class IocBean {
    private PojoInfo iocBean;
    public PojoInfo getIocBean() {
        return iocBean;
    }
    public void setIocBean(PojoInfo iocBean) {
        this.iocBean = iocBean;
    }
}