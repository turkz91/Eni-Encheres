package fr.eni.encheres.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.encheres.bll.BusinessException;
import fr.eni.encheres.bll.UtilisateurManager;
import fr.eni.encheres.bo.Utilisateur;

/**
 * Servlet implementation class UtilisateurInscription
 */
@WebServlet("/nouveau-compte")
public class UtilisateurInscription extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/user/inscription.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String pseudo = request.getParameter("pseudo");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String email = request.getParameter("email");
		String telephone = request.getParameter("telephone");
		String rue = request.getParameter("rue");
		String codePostal = request.getParameter("code-postal");
		String ville = request.getParameter("ville");
		String motDePasse = request.getParameter("mot-de-passe");
		String confirmation = request.getParameter("confirmation");

		UtilisateurManager utilisateurManager = new UtilisateurManager();
		Utilisateur utilisateur = new Utilisateur(pseudo, nom, prenom, email,telephone, rue, codePostal, ville,
				confirmation, 0, false);
		Boolean errorSaver = true;

		try {
			utilisateur = utilisateurManager.ajouterUtilisateur(pseudo, nom, prenom, email, telephone, rue,
					codePostal, ville, motDePasse, confirmation);
		} catch (Exception ex) {
			if (ex instanceof BusinessException) {
				request.setAttribute("listeCodesErreur", ((BusinessException) ex).getListeCodesErreur());
				ex.printStackTrace();
			} else {
				List<Integer> listeCodesErreur = new ArrayList<>();
				listeCodesErreur.add(CodesResultatServlets.FORMAT_UTILISATEUR_ERREUR);
				request.setAttribute("listeCodesErreur", listeCodesErreur);
			}
			errorSaver = false;
		}
		if (errorSaver) {
			HttpSession session = request.getSession();
			session.setAttribute("user", utilisateur);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/user/compte.jsp");
			rd.forward(request, response);
		} else {
			request.setAttribute("user", utilisateur);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/user/inscription.jsp");
			rd.forward(request, response);
		}
	}

}
