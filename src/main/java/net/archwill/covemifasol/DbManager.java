package net.archwill.covemifasol;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.archwill.covemifasol.entities.*;

public class DbManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(DbManager.class);

  private static DbManager INSTANCE = null;
  public static DbManager Instance() throws Exception {
    if (INSTANCE == null) {
      synchronized (DbManager.class) {
        if (INSTANCE == null) {
          INSTANCE = new DbManager();
        }
      }
    }
    return INSTANCE;
  }

  private Connection connection = null;

  private DbManager() throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    connection = DriverManager.getConnection("jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl", "VIGNEAUL", "oracle1");
    connection.setAutoCommit(true);
  }

  private PreparedStatement allItems = null;
  public List<Item> findAllItems() throws Exception {
    if (allItems == null) allItems = connection.prepareStatement(
      "SELECT * FROM ITEMS I1 INNER JOIN ITEMLIVRES L ON L.ITEM = I1.ID UNION " +
      "SELECT * FROM ITEMS I2 INNER JOIN ITEMDVDS M ON M.ITEM = I2.ID UNION " +
      "SELECT * FROM ITEMS I3 INNER JOIN ITEMDISQUES A ON A.ITEM = I3.ID"
    );
    ResultSet rs = allItems.executeQuery();
    List<Item> items = new ArrayList<Item>();
    while (rs.next()) {
      int type = rs.getInt(2);
      if (type == 1) {
        items.add(new AlbumItem(rs));
      } else if (type == 2) {
        items.add(new MovieItem(rs));
      } else if (type == 3) {
        items.add(new BookItem(rs));
      } else {
        items.add(new Item(rs));
      }
    }
    return items;
  }

  private PreparedStatement itemById = null;
  public Item findItemById(int id) throws Exception {
    if (itemById == null) itemById = connection.prepareStatement(
      "SELECT * FROM ITEMS I1 INNER JOIN ITEMLIVRES L ON L.ITEM = I1.ID WHERE I1.ID = ? UNION " +
      "SELECT * FROM ITEMS I2 INNER JOIN ITEMDVDS M ON M.ITEM = I2.ID WHERE I2.ID = ? UNION " +
      "SELECT * FROM ITEMS I3 INNER JOIN ITEMDISQUES A ON A.ITEM = I3.ID WHERE I3.ID = ?"
    );
    itemById.setInt(1, id);
    itemById.setInt(2, id);
    itemById.setInt(3, id);
    ResultSet rs = itemById.executeQuery();
    if (rs.next()) {
      int type = rs.getInt(2);
      if (type == 1) {
        return new AlbumItem(rs);
      } else if (type == 2) {
        return new MovieItem(rs);
      } else if (type == 3) {
        return new BookItem(rs);
      } else {
        return new Item(rs);
      }
    } else {
      return null;
    }
  }

  private PreparedStatement itemsByGenre = null;
  public List<Item> findItemsByGenre(int genre) throws Exception {
    if (itemsByGenre == null) itemsByGenre = connection.prepareStatement(
      "SELECT * FROM ITEMS I1 INNER JOIN ITEMLIVRES L ON L.ITEM = I1.ID WHERE I1.GENRE = ? UNION " +
      "SELECT * FROM ITEMS I2 INNER JOIN ITEMDVDS M ON M.ITEM = I2.ID WHERE I2.GENRE = ? UNION " +
      "SELECT * FROM ITEMS I3 INNER JOIN ITEMDISQUES A ON A.ITEM = I3.ID WHERE I3.GENRE = ?"
    );
    itemsByGenre.setInt(1, genre);
    itemsByGenre.setInt(2, genre);
    itemsByGenre.setInt(3, genre);
    ResultSet rs = itemsByGenre.executeQuery();
    List<Item> items = new ArrayList<Item>();
    while (rs.next()) {
      int type = rs.getInt(2);
      if (type == 1) {
        items.add(new AlbumItem(rs));
      } else if (type == 2) {
        items.add(new MovieItem(rs));
      } else if (type == 3) {
        items.add(new BookItem(rs));
      } else {
        items.add(new Item(rs));
      }
    }
    return items;
  }


  private PreparedStatement allGenres = null;
  public List<Genre> findAllGenres() throws Exception {
    if (allGenres == null) allGenres = connection.prepareStatement("SELECT * FROM GENRES");
    ResultSet rs = allGenres.executeQuery();
    List<Genre> genres = new ArrayList<Genre>();
    while (rs.next()) {
      genres.add(new Genre(rs));
    }
    return genres;
  }

  private PreparedStatement genreById = null;
  public Genre findGenreById(int id) throws Exception {
    if (genreById == null) genreById = connection.prepareStatement("SELECT * FROM GENRES G WHERE G.ID = ?");
    genreById.setInt(1, id);
    ResultSet rs = genreById.executeQuery();
    if (rs.next()) {
      return new Genre(rs);
    } else {
      return null;
    }
  }

  private PreparedStatement bookGenres = null;
  public List<Genre> findBookGenres() throws Exception {
    if (bookGenres == null) bookGenres = connection.prepareStatement("SELECT * FROM GENRES G WHERE G.ITEMTYPE = 3");
    ResultSet rs = bookGenres.executeQuery();
    List<Genre> genres = new ArrayList<Genre>();
    while (rs.next()) {
      genres.add(new Genre(rs));
    }
    return genres;
  }

  private PreparedStatement movieGenres = null;
  public List<Genre> findMovieGenres() throws Exception {
    if (movieGenres == null) movieGenres = connection.prepareStatement("SELECT * FROM GENRES G WHERE G.ITEMTYPE = 2");
    ResultSet rs = movieGenres.executeQuery();
    List<Genre> genres = new ArrayList<Genre>();
    while (rs.next()) {
      genres.add(new Genre(rs));
    }
    return genres;
  }

  private PreparedStatement albumGenres = null;
  public List<Genre> findAlbumGenres() throws Exception {
    if (albumGenres == null) albumGenres = connection.prepareStatement("SELECT * FROM GENRES G WHERE G.ITEMTYPE = 1");
    ResultSet rs = albumGenres.executeQuery();
    List<Genre> genres = new ArrayList<Genre>();
    while (rs.next()) {
      genres.add(new Genre(rs));
    }
    return genres;
  }


  private PreparedStatement allBooks = null;
  public List<BookItem> findAllBooks() throws Exception {
    if (allBooks == null) allBooks = connection.prepareStatement("SELECT * FROM ITEMS I INNER JOIN ITEMLIVRES L ON L.ITEM = I.ID");
    ResultSet rs = allBooks.executeQuery();
    List<BookItem> items = new ArrayList<BookItem>();
    while (rs.next()) {
      items.add(new BookItem(rs));
    }
    return items;
  }

  private PreparedStatement booksByGenre = null;
  public List<BookItem> findBooksByGenre(int genre) throws Exception {
    if (booksByGenre == null) booksByGenre = connection.prepareStatement("SELECT * FROM ITEMS I INNER JOIN ITEMLIVRES L ON L.ITEM = I.ID WHERE I.GENRE = ?");
    booksByGenre.setInt(1, genre);
    ResultSet rs = booksByGenre.executeQuery();
    List<BookItem> items = new ArrayList<BookItem>();
    while (rs.next()) {
      items.add(new BookItem(rs));
    }
    return items;
  }


  private PreparedStatement allMovies = null;
  public List<MovieItem> findAllMovies() throws Exception {
    if (allMovies == null) allMovies = connection.prepareStatement("SELECT * FROM ITEMS I INNER JOIN ITEMDVDS M ON M.ITEM = I.ID");
    ResultSet rs = allMovies.executeQuery();
    List<MovieItem> items = new ArrayList<MovieItem>();
    while (rs.next()) {
      items.add(new MovieItem(rs));
    }
    return items;
  }

  private PreparedStatement moviesByGenre = null;
  public List<MovieItem> findMoviesByGenre(int genre) throws Exception {
    if (moviesByGenre == null) moviesByGenre = connection.prepareStatement("SELECT * FROM ITEMS I INNER JOIN ITEMDVDS M ON M.ITEM = I.ID WHERE I.GENRE = ?");
    moviesByGenre.setInt(1, genre);
    ResultSet rs = moviesByGenre.executeQuery();
    List<MovieItem> items = new ArrayList<MovieItem>();
    while (rs.next()) {
      items.add(new MovieItem(rs));
    }
    return items;
  }


  private PreparedStatement allAlbums = null;
  public List<AlbumItem> findAllAlbums() throws Exception {
    if (allAlbums == null) allAlbums = connection.prepareStatement("SELECT * FROM ITEMS I INNER JOIN ITEMDISQUES A ON A.ITEM = I.ID");
    ResultSet rs = allAlbums.executeQuery();
    List<AlbumItem> items = new ArrayList<AlbumItem>();
    while (rs.next()) {
      items.add(new AlbumItem(rs));
    }
    return items;
  }

  private PreparedStatement albumsByGenre = null;
  public List<AlbumItem> findAlbumsByGenre(int genre) throws Exception {
    if (albumsByGenre == null) albumsByGenre = connection.prepareStatement("SELECT * FROM ITEMS I INNER JOIN ITEMDISQUES A ON A.ITEM = I.ID WHERE I.GENRE = ?");
    albumsByGenre.setInt(1, genre);
    ResultSet rs = albumsByGenre.executeQuery();
    List<AlbumItem> items = new ArrayList<AlbumItem>();
    while (rs.next()) {
      items.add(new AlbumItem(rs));
    }
    return items;
  }
}
