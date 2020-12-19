package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String context_path=((HttpServletRequest)request).getContextPath();
        String servlet_path=((HttpServletRequest)request).getServletPath();

        if(!servlet_path.matches("/css.*")){
            HttpSession session=((HttpServletRequest)request).getSession();

            //セッションスコープに登録された従業員情報を取得
            Employee e=(Employee)session.getAttribute("login_employee");

            if(!servlet_path.equals("/login")){
                //ログアウト状態ならログイン画面にリダイレクト
                if(e==null){
                    ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                    return;
                }
                //従業員の登録表は管理者だけが見れるようにしておく
                if(servlet_path.matches("/employees.*")&& e.getAdmin_flag()==0){
                    ((HttpServletResponse)response).sendRedirect(context_path+"/");
                    return;
                }
            }else{
                //ログインしてるのにログイン画面を表示させようとしていたら
                //トップページにリダイレクト
                if(e !=null){
                    ((HttpServletResponse)response).sendRedirect(context_path+"/");
                    return;
                }
            }
        }
        chain.doFilter(request,response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
