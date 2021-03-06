package fr.eni.encheres.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.eni.encheres.bll.ArticleEnchereManager;
import fr.eni.encheres.bll.BusinessException;
import fr.eni.encheres.bo.Categorie;

/**
 * Servlet implementation class InstallArticleVenduDAL
 */
@WebServlet("/nouvelleVente")
public class ServletNouvelleVente extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<Categorie> listeCategories = new ArrayList<>();
		ArticleEnchereManager articleManager = new ArticleEnchereManager();
		
		try {
			listeCategories = articleManager.selectAllCategorie();
			if (listeCategories != null) {
				request.setAttribute("listeCategories", listeCategories);
			}
		}  catch (BusinessException e) {
		}
		
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/article/venteArticle.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// infos article
		String nom_article = request.getParameter("nom");
		String description = request.getParameter("description");
		LocalDate date_debut_encheres = LocalDate.parse(request.getParameter("debut_enchere"));
		LocalDate date_fin_encheres = LocalDate.parse(request.getParameter("fin_enchere"));
		int prix_initial = Integer.valueOf(request.getParameter("prix_initial"));

		// no_utilisateur dans infos utilisateur
		int no_categorie = Integer.valueOf(request.getParameter("categorie"));

		// infos utilisateur
		int no_utilisateur = Integer.valueOf(request.getParameter("no_utilisateur"));


//		String rue = request.getParameter("rue");
//		String codePostal = request.getParameter("code_postal");
//		String ville = request.getParameter("ville");

		ArticleEnchereManager articleManager = new ArticleEnchereManager();

		try {
			articleManager.ajouterArticle(nom_article, description, date_debut_encheres, date_fin_encheres,
					prix_initial, no_utilisateur, no_categorie);

		} catch (BusinessException ex) {
			request.setAttribute("listeCodesErreur", ex.getListeCodesErreur());
			ex.printStackTrace();
		} catch (Exception ex) {
			List<Integer> listeCodesErreur = new ArrayList<>();
			listeCodesErreur.add(CodesResultatServlets.FORMAT_ARTICLE_ERREUR);
			request.setAttribute("listeCodesErreur", listeCodesErreur);
		}
			
		RequestDispatcher rd = request.getRequestDispatcher("");
		rd.forward(request, response);
	}

}
