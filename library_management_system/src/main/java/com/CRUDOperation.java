package com;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CRUDOperation {


     // =========================================================================
    //  SECTION 1 — BOOK OPERATIONS
    // =========================================================================

    public static void addBook (string title, string author, int categoryId, int quantity){
        String sql = "INSERT INTO BOOOKS (title, author, category_Id, quantity, available)"
                + "VALUES (?,?,?,?,?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;

        try (PreparedStatement ps == conn.prepareStatement (sql)){
            ps.setString(parameterIndex: 1, title);
            ps.setString(parameterIndex: 2, author);
            ps.setInt   (parameterIndex: 3, category_Id);
            ps.setInt   (parameterIndex: 4, quantity);
            ps.setInt   (parameterIndex: 5, quantity);
            int rows = ps. executeUpdate ()
            if (rows > 0) {
                System.out.println("\n[SUCCESS] Book \""+ title + "\ added sucessfully.");

            }
        }catch (SQLException e) {
            System.out.println("[ERROR] Failed to add book: "+ e. getMessage());
        }finally {
            DBConnection.closeConnection(conn);
        }
}

//── 1B. VIEW ALL BOOKS (READ) ─────────────────────────────────────────────
public static void viewAllBooks() {
    String sql = "SELECT b.book_id, b.title, b.author,c.category_name, "
             + "b.quantity, b.available"
             + "FROM BOOKS b"
             + "JOIN CATEGORY c ON b.category_id = c.category"
             + "ORDER BY b.book_id";
Connection conn = DBConnection.getConnection();
if (conn == null) return;

try (PreparedStatement ps = conn prepareStatement (sql);
    ResultSet rs = ps.executeQuery()) {
    
    System.out.println("\n" + "=" .repeat (count: 80));
    System.out.println(format: "%-6s %-30s %-20s %-15s %-6s %-9s%n",
        ...args : "ID", "TITLE", "AUTHOR", "CATEGORY", "QTY", "AVAILABLE")
    System.out,println("=" .repeat(count:80));
    
    boolen hasData = false
    while (rs.next()) {
        hasData = true
        System.out.printf(format: "%-6d %-30s %-20s %-15s %-6d %-9d%n",
            rs.getInt (columnLabel: "book_id"),
            rs.getString(columnLabel; "title");
            rs.getString(columnLabel: "author");
            rs.getString(column: "quantity");
            rs.getInt(column: "quantity");
            rs.getInt(column: "available"));

    } if(!hasData) {
        System.out.println("No books is found in the library.");
    } 
    System.out.println("=".repeat(80));

        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to retrieve books: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
