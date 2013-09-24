package jp.arcanum.clicklog.page;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import jp.arcanum.clicklog.page.control.Contents;
import net.sf.click.control.Column;
import net.sf.click.control.Submit;
import net.sf.click.control.Table;


public class MainPage extends AbstractPage{
    

    // ---- コンテンツ関係
    
    public Table con_contents = new Table("con_contents");

    private Submit    con_new = new Submit("con_new","新規投稿"){
        
        public String toString(){
            HttpSession sess = super.getContext().getSession();
            if(sess.getAttribute("LOGIN")==null){
                return "";
            }
            return super.toString();
        }
    };
    
    /**
     * コンストラクタ
     *
     */
    public MainPage(){
        this.con_new.setListener(this, "clickNew");
        super.form.add(this.con_new);
        
        Column col = new Column("CONTENTS","日記");
        col.setEscapeHtml(false);
        this.con_contents.addColumn(col);
        this.con_contents.setAttribute("class", super.tablestype);
        this.con_contents.setWidth("100%");
        super.addControl(this.con_contents);
        
        
    }
    
    /**
     * セキュリティチェック
     */
    public boolean onSecurityCheck(){
        return true;
    }
    

    /**
     * 新規投稿ボタン処理
     * @return
     */    
    public boolean clickNew(){
        
        EditBlogPage next = (EditBlogPage)super.getContext().createPage(EditBlogPage.class);
        
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateformat = new SimpleDateFormat( "yyyyMMdd" );
        String yyyymmdd = dateformat.format(date);
        
        next.setDate(yyyymmdd);
        super.setForward(next);
        
        return false;
        
    }
    
    /**
     * 描画前処理
     */
    public void onRender(){
        super.onRender();
        this.setContents();   
    }
    
    /**
     * コンテンツ設定
     *
     */
    private void setContents(){
        
        String path = "/WEB-INF/data/blog/";
        String realpath = super.getContext().getServletContext().getRealPath(path);
        
        HttpSession sess = super.getContext().getSession();
        Calendar cal = (Calendar)sess.getAttribute("CALENDAR");
        if(cal == null){
            cal = Calendar.getInstance();
        }
        String y = Integer.toString(cal.get(Calendar.YEAR));
        String m = "00" + Integer.toString(cal.get(Calendar.MONTH) + 1 );
        m = m.substring(m.length()-2);
        String ym = y+m;
        
        
        File dir = new File(realpath);
        File[] filelist = dir.listFiles();
        TreeMap filetree = new TreeMap(new Comparator(){
            public int compare( Object object1, Object object2 ){
                return ( (Comparable)object1 ).compareTo( object2 ) * -1;
            }
        });
        
        for(int i = 0 ; i < filelist.length ; i++){
            if(filelist[i].getName().startsWith(ym)){
                filetree.put(filelist[i].getName(), filelist[i]);
            }
            
        }

        List rowlist = new ArrayList();
        
        Iterator ite = filetree.keySet().iterator();
        while(ite.hasNext()){

            Map row = new HashMap();
            
            File f = (File)filetree.get(ite.next());
            String yyyymmdd = f.getName().substring(0,f.getName().lastIndexOf("."));
            yyyymmdd = yyyymmdd.substring(0,8);
            
            Contents con = new Contents(yyyymmdd,false);
            
            con.setAttribute("onclick", "linkymd.value='" + con.getName()+"';showblog.click();return false;");

            
            super.addControl(con);
            row.put("CONTENTS", con);
            rowlist.add(row);
            
        }
        
        this.con_contents.setRowList(rowlist);                
        
        
    }

}

