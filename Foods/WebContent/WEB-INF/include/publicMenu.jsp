 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--_menu 作为公共模版分离出去-->
<aside class="Hui-aside">

    <div class="menu_dropdown bk_2">
        <dl id="menu-member">
            <b style="margin-left: 16px">上次登陆时间：</b><br/><span style="margin-left: 45px">${lastLoginTime}</span>
            <hr style="border-color: #e5e5e5;"/>
        </dl>
        <dl id="menu-member">
            <dt><i class="Hui-iconfont">&#xe60d;</i> 个人空间模块管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/customer/list" title="用户列表">用户列表</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-product">
            <dt><i class="Hui-iconfont">&#xe620;</i> 菜单管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/product/list" title="商品列表">菜单列表</a></li>
                    <li><a href="${pageContext.request.contextPath}/category/list" title="商品产品分类管理">菜单分类管理</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-comments">
            <dt><i class="Hui-iconfont">&#xe622;</i> 评论管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/review/list" title="评论列表">评论列表</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-picture">
            <dt><i class="Hui-iconfont">&#xe613;</i> 订单管理(物流管理)<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/order/list" title="订单列表">订单列表</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-admin">
            <dt><i class="Hui-iconfont">&#xe62d;</i> 管理员管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/config/listRole" title="角色管理">角色管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/config/listPermission" title="权限管理">权限管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/config/listUser" title="管理员列表">管理员列表</a></li>
                </ul>
            </dd>
        </dl><%-- 
        <c:if test="${subject.hasRole('admin')}">
            <dl id="menu-system">
                <dt><i class="Hui-iconfont">&#xe62e;</i> 资讯管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
                <dd>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/zixun/list" title="资讯列表">资讯列表</a></li>
                    </ul>
                </dd>
            </dl>
        </c:if> --%>
    </div>
</aside>
<div class="dislpayArrow hidden-xs"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<!--/_menu 作为公共模版分离出去-->
