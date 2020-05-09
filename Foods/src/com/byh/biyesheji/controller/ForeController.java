package com.byh.biyesheji.controller;

import com.byh.biyesheji.pojo.*;
import com.byh.biyesheji.service.*;
import com.byh.biyesheji.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 鍓嶅彴鎵�鏈夎姹俢ontroller
 */
@Controller
@RequestMapping("/fore")
public class ForeController {

    @Autowired
    private ForeService foreService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ZiXunService ziXunService;

    public String PNAME=null;

    /**
     * 鍓嶅彴棣栭〉
     * @param model
     * @return
     */
    @RequestMapping("/foreIndex")
    public String index(Model model,HttpSession session){

        //浼犲叆3涓垎绫�
        List<Category> categories = foreService.listToThree();
        List<Category> categories1 = categoryService.list();
        //缁欐瘡涓垎绫昏缃晢鍝�
        for (Category c:categories){
            List<Product> products = productService.getProductsByCid(c.getId());
            //濡傛灉鍒嗙被涓嬬殑鍟嗗搧瓒呰繃4涓紝鍒欏彧鏄剧ず4涓粰鍓嶇
            if(products.size()>5){
                List<Product> products1 = new ArrayList<>();
                for(int i=0;i<=4;i++){
                    products1.add(products.get(i));
                }
                c.setProducts(products1);
            }else{
                c.setProducts(products);
            }
        }
        model.addAttribute("categories",categories);
        session.setAttribute("categories",categories1); //淇濆瓨鍦╯ession  浣垮叾浠栭〉闈篃鑳借幏鍙栧埌鍒嗙被鍒楄〃 鑰屼笉鐢ㄦ瘡娆￠兘鍘绘煡璇�
        return "forepage/index2";
    }


    /**
     * 鍟嗗搧璇︽儏璺宠浆
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/foreDetailUI")
    public String detailUI(@RequestParam(value = "id")int id,Model model){
        Product product = productService.get(id);
        if(product==null) return "forepage/noPro";

        User user = productService.getUserByBid(product.getBid());
        Category category = productService.getCategoryByCid(product.getCid());
        product.setCategory(category);
        product.setUser(user);

        List<Product> fivePro = foreService.getFivePro();

        model.addAttribute("product",product);
        model.addAttribute("fivePro",fivePro);

        List<Review> list = reviewService.getReviewListByPid(id);
        model.addAttribute("reviews",list);
        model.addAttribute("rs",list.size());

        return "forepage/proDetail";
    }

    /**
     * 娉ㄥ唽椤甸潰
     * @return
     */
    @RequestMapping("/foreRegisterUI")
    public String registerUI(){
        return "forepage/foreRegister";
    }

    /**
     * 鐧婚檰椤甸潰
     * @return
     */
    @RequestMapping("/foreLoginUI")
    public String foreLoginUI(){
        return "forepage/forelogin";
    }

    /**
     * 妯℃�佺獥鍙ｇ櫥闄� 楠岃瘉
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/foreMtLogin")
    @ResponseBody
    public String foreIsLogin(Customer customer,HttpSession session){
        Customer cst = customerService.foreLogin(customer);
        if(null==cst){
            return "false";
        }
        session.setAttribute("cst", cst);
        return "true";
    }

    /**
     * ajax鍒ゆ柇瀹㈡埛鏄惁鐧婚檰
     * @param session
     * @return
     */
    @RequestMapping("/foreIsLogin")
    @ResponseBody
    public String isLogin(HttpSession session){
        Customer cst = (Customer) session.getAttribute("cst");
        return cst==null?"false":"true";
    }

    /**
     * 娉ㄥ唽
     * @param customer
     * @return
     */
    @RequestMapping("/foreRegister")
    public String register(Customer customer){
        customer.setStatus(0);
        customerService.save(customer);
        return "forepage/registerSuccess";
    }

    /**
     * 瀹㈡埛鐧婚檰
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/foreLogin")
    public String foreLogin(Customer customer, HttpSession session,Model model){
        Customer cst = customerService.foreLogin(customer);
        if (cst!=null){
            session.setAttribute("cst",cst);
            return "redirect:foreIndex";
        }else {
            return "redirect:foreLoginMsg";
        }
    }

    /**
     * 鐢ㄦ埛鐧婚檰杩斿洖淇℃伅
     * @return
     */
    @RequestMapping("/foreLoginMsg")
    public String foreLoginMsg(HttpServletRequest request){
        request.setAttribute("msg","true");
        return "forepage/forelogin";
    }

    /**
     * 瀹㈡埛娉ㄩ攢
     * @param session
     * @return
     */
    @RequestMapping("/foreCstLoginOut")
    public String cstLoginOut(HttpSession session){
        session.setAttribute("cst",null);
        return "redirect:foreIndex";
    }

    /**
     * 绔嬪嵆璐拱
     * @param session
     * @param pid  鍟嗗搧id
     * @param number  鍟嗗搧鏁伴噺
     * @return  閲嶅畾鍚戝埌鏀粯 锛� 浼犲叆璁㈠崟椤筰d
     */
    @RequestMapping("/forebuyone")
    public String forebuyone(HttpSession session,int pid,int number,float totalPrice){
        Customer cst = (Customer) session.getAttribute("cst");
        Product product = productService.get(pid);

        int oiid = 0;

        boolean find = false;
        List<OrderItem> orderItems = orderItemService.listByCustomer(cst.getId());//鑾峰緱璁㈠崟椤硅〃涓鐢ㄦ埛鐨勬墍鏈夎鍗昳d涓虹┖鐨勮鍗曢」
        for (OrderItem oi : orderItems) {
            //鍩轰簬鐢ㄦ埛瀵硅薄customer锛屾煡璇㈡病鏈夌敓鎴愯鍗曠殑璁㈠崟椤归泦鍚�
            // 濡傛灉浜у搧鏄竴鏍风殑璇濓紝灏辫繘琛屾暟閲忚拷鍔�
            if(oi.getProduct().getId().intValue()==product.getId().intValue()){
                //濡傛灉宸茬粡瀛樺湪杩欎釜浜у搧瀵瑰簲鐨凮rderItem锛屽苟涓旇繕娌℃湁鐢熸垚璁㈠崟锛屽嵆杩樺湪璐墿杞︿腑銆� 閭ｄ箞灏卞簲璇ュ湪瀵瑰簲鐨凮rderItem鍩虹涓婏紝璋冩暣鏁伴噺
                oi.setNumber(oi.getNumber()+number);
                orderItemService.update(oi);
                find = true;
                //鑾峰彇杩欎釜璁㈠崟椤圭殑 id
                oiid = oi.getId();
                break;
            }
        }
        //濡傛灉涓嶅瓨鍦ㄥ搴旂殑OrderItem,閭ｄ箞灏辨柊澧炰竴涓鍗曢」OrderItem
        if(!find){
            OrderItem oi = new OrderItem();
            oi.setCstid(cst.getId());
            oi.setNumber(number);
            oi.setPid(pid);
            orderItemService.save(oi);
            //鑾峰彇杩欎釜鍒氭坊鍔犵殑璁㈠崟椤圭殑 id
            oiid = oi.getId();
        }

        return "redirect:forebuy?oiid="+oiid;
    }

    /**
     * 绔嬪嵆璐拱銆佽喘鐗╄溅鎻愪氦鍒拌鍗曢〉闈㈣皟鐢�  鏍规嵁oiid璁＄畻璁㈠崟椤圭殑鎬讳环銆佽喘涔版暟閲� 锛� 璁㈠崟椤规斁session
     * 璁㈠崟-鏀粯  涓婁竴娆＄殑璐墿淇℃伅浼氳涓嬫鍗曚釜鎸ゆ帀  鏍规嵁oiid鑾峰緱璁㈠崟椤�
     * @param model
     * @param oiid 绔嬪嵆璐拱鐢熸垚鐨勮鍗曢」id
     * @param session
     * @return 杩斿洖璁㈠崟椤归泦鍚�   |   杩斿洖鎵�鏈夎鍗曢」鍔犺捣鏉ョ殑鎬讳环
     */
    @RequestMapping("/forebuy")
    public String forebuy(Model model,String[] oiid,HttpSession session){
        System.out.println(oiid);

        List<OrderItem> ois = new ArrayList<>();

        Customer cst = (Customer)session.getAttribute("cst");

        float total = 0;
        int number = 0;
        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi= orderItemService.get(id);
            if (cst.getStatus()==1){
                total +=oi.getProduct().getPrice()*0.8*oi.getNumber();
            }else{
                total +=oi.getProduct().getPrice()*oi.getNumber();
            }
            number += oi.getNumber();
            ois.add(oi);
        }
        /*
          绱杩欎簺ois鐨勪环鏍兼�绘暟锛岃祴鍊煎湪total涓�
          鎶婅鍗曢」闆嗗悎鏀惧湪session鐨勫睘鎬� "ois" 涓�,鏂逛究涓嬭鍗曟椂鍊欑洿鎺ヨ幏鍙�
          鎶婃�讳环鏍兼斁鍦� model鐨勫睘鎬� "total" 涓�
          鏈嶅姟绔烦杞埌buy.jsp
          */
        session.setAttribute("ois", ois);
        model.addAttribute("total", total);
        model.addAttribute("number", number);

        return "forepage/foreBuy";
    }

    /**
     * 娣诲姞璐墿杞�
     * @param pid  鍟嗗搧id
     * @param number  璐拱鏁伴噺
     * @param model
     * @param session
     * @return  boolean
     */
    @RequestMapping("/foreAddCart")
    @ResponseBody
    public String addCart(int pid, int number, Model model,float totalPrice,HttpSession session) {
        Customer customer =(Customer)  session.getAttribute("cst");
        if(customer==null){
            return "false";
        }
        Product p = productService.get(pid);

        boolean found = false;
        //鑾峰緱璁㈠崟椤硅〃涓鐢ㄦ埛鐨勬墍鏈夎鍗昳d涓虹┖鐨勮鍗曢」
        List<OrderItem> ois = orderItemService.listByCustomer(customer.getId());
        for (OrderItem oi : ois) {
            //鍩轰簬鐢ㄦ埛瀵硅薄customer锛屾煡璇㈡病鏈夌敓鎴愯鍗曠殑璁㈠崟椤归泦鍚�
            // 濡傛灉浜у搧鏄竴鏍风殑璇濓紝灏辫繘琛屾暟閲忚拷鍔�
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){
                //濡傛灉宸茬粡瀛樺湪杩欎釜浜у搧瀵瑰簲鐨凮rderItem锛屽苟涓旇繕娌℃湁鐢熸垚璁㈠崟锛屽嵆杩樺湪璐墿杞︿腑銆� 閭ｄ箞灏卞簲璇ュ湪瀵瑰簲鐨凮rderItem鍩虹涓婏紝璋冩暣鏁伴噺
                oi.setNumber(oi.getNumber()+number);
                orderItemService.update(oi);
                found = true;
                break;
            }
        }
        //濡傛灉涓嶅瓨鍦ㄥ搴旂殑OrderItem,閭ｄ箞灏辨柊澧炰竴涓鍗曢」OrderItem
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setCstid(customer.getId());
            oi.setNumber(number);
            oi.setPid(pid);
            orderItemService.save(oi);
        }

        return "success";
    }

    /**
     * 鏌ョ湅璐墿杞﹁喘鐗╄溅
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/forecart")
    public String cart( Model model,HttpSession session) {
        Customer customer =(Customer)  session.getAttribute("cst");
        //cstid绛変簬褰撳墠鐧婚檰鐢ㄦ埛id 骞朵笖oid涓簄ull鐨勮鍗曢」
        List<OrderItem> ois = orderItemService.listByCustomer(customer.getId());
        //璐墿杞︽病鏈夊晢鍝�
        if(ois==null || ois.size()==0){
            return "forepage/cart_noPro";
        }
        int totalProductNumber = 0;
        for (OrderItem oi:ois){
            totalProductNumber += oi.getNumber();
        }
        model.addAttribute("ois", ois);
        model.addAttribute("size", totalProductNumber);

        return "forepage/foreCart";
    }

    /**
     * 鍒犻櫎璁㈠崟椤�
     * @param oiid 璁㈠崟椤筰d
     * @param session
     * @return
     */
    @RequestMapping("/foreDelOrderItem")
    @ResponseBody
    public String foreDelOrderItem(int oiid,HttpSession session){
        Customer customer = (Customer) session.getAttribute("cst");
        if(customer==null){
            return "noSuccess";
        }
        orderItemService.del(oiid);
        return "success";
    }

    /*
      鐐瑰嚮鎻愪氦璁㈠崟
    1. 浠巗ession涓幏鍙朿st瀵硅薄
    2. 閫氳繃鍙傛暟Order鎺ュ彈鏀惰揣浜�
    3. 鏍规嵁褰撳墠鏃堕棿鍔犱笂涓�涓�4浣嶉殢鏈烘暟鐢熸垚璁㈠崟鍙�
    4. 鏍规嵁涓婅堪鍙傛暟锛屽垱寤鸿鍗曞璞�
    5. 鎶婅鍗曠姸鎬佽缃负鏈敮浠�
    6. 浠巗ession涓幏鍙栬鍗曢」闆嗗悎 ( 鍦ㄧ粨绠楀姛鑳界殑ForeController.buy() 13琛岋紝璁㈠崟椤归泦鍚堣鏀惧埌浜唖ession涓� )
    7. 鎶婅鍗曞姞鍏ュ埌鏁版嵁搴擄紝骞朵笖閬嶅巻璁㈠崟椤归泦鍚堬紝璁剧疆姣忎釜璁㈠崟椤圭殑order锛屾洿鏂板埌鏁版嵁搴�
    8. 缁熻鏈璁㈠崟鐨勬�婚噾棰�
    9. 瀹㈡埛绔烦杞埌纭鏀粯椤礷orePayed锛屽苟甯︿笂璁㈠崟id鍜屾�婚噾棰�
     */
    @RequestMapping("/foreCreateOrder")
    public String createOrder( Model model,String address,HttpSession session){
        /*
          鎻愪氦璁㈠崟鍚庯紝璁剧疆code锛屽鎴穒d锛屾敮浠樼姸鎬侊紝鍦板潃
         */
        Order order = new Order();
        Customer customer =(Customer)  session.getAttribute("cst");
        Random random = new Random();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + random.nextInt(10000);
        order.setCode(orderCode);
        order.setAddress(address);
        order.setCstid(customer.getId());
        order.setStatus(0);//鏈敮浠�

        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");
        //缁欐瘡涓鍗曢」璁剧疆璁㈠崟id  骞朵笖绠楀嚭璁㈠崟鎬讳环
        float total =orderService.add(order,ois);
        return "redirect:forePayed?oid="+order.getId() +"&total="+total;
    }

    /**
     * 鏀粯鎴愬姛璺宠浆
     * @param oid 璁㈠崟id
     * @param total 鎬讳环
     * @param model
     * @return
     */
    @RequestMapping("/forePayed")
    public String payed(int oid, float total, Model model) {
        Order order = orderService.get(oid);
        order.setStatus(1);
        orderService.update(order);
        model.addAttribute("total", total);

        return "forepage/forePayed";
    }

    /**
     * 鎴戠殑璁㈠崟  鏍规嵁session鏌ョ湅褰撳墠鐢ㄦ埛鐨勮鍗�
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/forebought")
    public String forebought(Model model,HttpSession session){
        Customer customer = (Customer) session.getAttribute("cst");
        List<Order> os= orderService.list(customer.getId());

        //缁欐瘡涓鍗曠殑璁㈠崟椤硅缃睘鎬у�硷紝濡俹rderitem銆乸roduct
        orderItemService.fill(os);

        model.addAttribute("os", os);
        return "forepage/foreBought";
    }

    /**
     * 鎼滅储鍟嗗搧
     * @param model
     * @param pName
     * @return
     */
    @RequestMapping("/foreNameLike")
    public String foreNameLike(Model model, String pName,Page page){
        PageHelper.offsetPage(page.getStart(),page.getCount());//鍒嗛〉鏌ヨ
        if(pName!=null) PNAME = pName;
        List<Product> products = productService.findByName(PNAME);
        int total = (int) new PageInfo<>(products).getTotal();//鎬绘潯鏁�
        page.setTotal(total);

        model.addAttribute("products",products);
        model.addAttribute("total",total);
        model.addAttribute("page", page);

        model.addAttribute("proSize",products.size());

        return "forepage/proSeach";
    }

    /**
     * 鏄剧ず鍒嗙被涓嬬殑鍟嗗搧
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/foreFindCategory")
    public String foreFindCategory(Model model,@RequestParam(value = "id") int cid){
        List<Product> ps = productService.findByCid(cid);
        Category category = categoryService.get(cid);
        if(ps.size()>8){
            List<Product> ps1 = new ArrayList<>();
            for(int i=0;i<8;i++){
                ps1.add(ps.get(i));
            }
            model.addAttribute("products",ps1);
            model.addAttribute("category",category);
            return "forepage/proCategorySeach";
        }
        model.addAttribute("products",ps);
        model.addAttribute("proSize",ps.size());
        model.addAttribute("category",category);

        return "forepage/proCategorySeach";
    }

    @RequestMapping("/faq")
    public String faq(){
        return "forepage/faq";
    }

    /**
     * 鍟嗗搧璇勪环
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("/forePingjia")
    public String forePingjia(int pid,Model model){

        return "forePage/pingjia";
    }

    /**
     * 鍟嗗搧璇勮
     * @param session
     * @param pid
     * @param content
     * @return
     */
    @RequestMapping("/cstPinglun")
    @ResponseBody
    public String cstPinglun(HttpSession session,int pid,String content){
        Customer cst = (Customer) session.getAttribute("cst");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        //string杞琩ate
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sdf.parse(format, pos);

        Review review = new Review();
        review.setCstid(cst.getId());
        review.setCustomer(cst);
        review.setPid(pid);
        review.setProduct(productService.get(pid));
        review.setContent(content);
        review.setCreatetime(strtodate);

        reviewService.save(review);

        return "success";
    }

    /**
     * 宸插鏍哥殑璧勮
     * @param model
     * @return
     */
    @RequestMapping("/foreZixuns")
    public String zixun(Model model){
        List<ZiXun> list = ziXunService.list();
        model.addAttribute("list",list);
        return "forepage/foreZixun";
    }

    @RequestMapping("/foreZixunadd")
    @ResponseBody
    public String zixunadd(String content,HttpSession session){
        Customer c = (Customer) session.getAttribute("cst");
        ZiXun z = new ZiXun();
        z.setCstid(c.getId());
        z.setContent(content);
        z.setFabudate(new Date());
        z.setStatus(0);
        ziXunService.save(z);
        return "success";
    }

}
