
package jp.arcanum.clicklog.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.click.ClickServlet;


public class ArClickServlet extends ClickServlet {


    /**
     * GET
     */
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response
	) throws ServletException, IOException {

		//request.setCharacterEncoding("UTF-8");
        this.showLog(request, response, "GET");
        super.doGet(request, response);
	}

    /**
     * POST
     */
	protected void doPost(
			HttpServletRequest request,
			HttpServletResponse response

	) throws ServletException, IOException {

		//request.setCharacterEncoding("UTF-8");
        this.showLog(request, response, "POST");
        super.doPost(request, response);
	}


    /**
     * リクエストを表示します
     * @param request
     * @param response
     * @param postget POST / GET
     */
    private void showLog(
            final HttpServletRequest request,
            final HttpServletResponse response ,
            final String postget){



        logger.debug(request.getRequestURL());
        logger.debug( " -- " + postget );
        logger.debug( "-+-----------------------------------------------------" ) ;
        logger.debug( " | SessionId = " + request.getSession().getId()  ) ;

        Enumeration enumParam = request.getParameterNames() ;
        while( enumParam.hasMoreElements() ){
            String str = ( String )enumParam.nextElement() ;
            logger.debug(" | " + str + " = '"   + request.getParameter( str ) +"'") ;

        }

        Enumeration enumattr = request.getSession().getAttributeNames() ;
        while( enumattr.hasMoreElements() ){
            String elem = (String)enumattr.nextElement();
            logger.debug( " | A Object in this Session ... " + elem ) ;
        }

        logger.debug(request.getContextPath());

        logger.debug( "-+-----------------------------------------------------" ) ;
    }

}
