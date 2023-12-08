import java.sql.*;
import java.util.Scanner;

public class CLI_ADMIN {

 private Database database;

 public CLI_ADMIN(Database database) {
  this.database = database;
 }

 public void run() throws SQLException {
  trang_chu();
 }

 private void trang_chu() throws SQLException {
  while (true) {
   System.out.println("-".repeat(100));
   System.out.println("Trang Admin\n");

   System.out.println("1. Xem danh sách sản phẩm");
   System.out.println("2. Xem danh sách hóa đơn");
   System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

   Scanner scanner = new Scanner(System.in);
   String command = scanner.nextLine();

   switch (command) {
    case "1":
     ds_san_pham("");
     break;
    case "2":
     ds_hoa_don();
     break;
    default:
     System.out.println("Lệnh không hợp lệ.");
     break;
   }
  }
 }

 private void ds_hoa_don() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  int ket_qua;

  System.out.println("-".repeat(100));

  System.out.println("Danh sách hóa đơn\n");

  CallableStatement statement = database.connection.prepareCall("{call ds_hoa_don_addmin()}");
  statement.execute();

  ResultSet resultSet = statement.getResultSet();

  System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", "Mã hóa đơn", "Ngày lập", "Ngày giao", "Thanh toán", "Giao hàng", "Mã khách hàng");
  while (resultSet.next()) {
   Integer id = resultSet.getInt("ma_hoa_don");
   Date invoice = resultSet.getDate("ngay_lap");
   Date delivery = resultSet.getDate("ngay_giao");
   int pay = resultSet.getInt("thanh_toan");
   int delivered = resultSet.getInt("giao_hang");
   Integer id_customer = resultSet.getInt("ma_khach_hang");
   System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", id, invoice, delivery, pay, delivered, id_customer);
  }
  while (true) {
   System.out.println("\n1. Xem chi tiết hóa đơn");
   System.out.println("2. Danh sách hóa đơn đã thanh toán nhưng chưa giao hàng");
   System.out.println("3. Trang chủ");
   System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

   String command = scanner.nextLine();
   switch (command) {
    case "1":
     System.out.print("Nhập mã hóa đơn mà bạn muốn xem:");
     int ma_hoa_don = scanner.nextInt();
     xem_tt_hd(ma_hoa_don);
     break;
    case "2":
     ds_hd_thanh_toan();
     break;
    case "3":
     trang_chu();
     break;
    default:
     System.out.println("Lệnh không hợp lệ.");
     break;
   }
  }
 }

 private void ds_hd_thanh_toan() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  int ket_qua;

  System.out.println("-".repeat(100));

  System.out.println("Danh sách hóa đơn đã thanh toán nhưng chưa giao hàng\n");

  CallableStatement statement = database.connection.prepareCall("{call ds_hd_thanh_toan()}");
  statement.execute();

  ResultSet resultSet = statement.getResultSet();

  System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", "Mã hóa đơn", "Ngày lập", "Ngày giao", "Thanh toán", "Giao hàng", "Mã khách hàng");
  while (resultSet.next()) {
   Integer id = resultSet.getInt("ma_hoa_don");
   Date invoice = resultSet.getDate("ngay_lap");
   Date delivery = resultSet.getDate("ngay_giao");
   int pay = resultSet.getInt("thanh_toan");
   int delivered = resultSet.getInt("giao_hang");
   Integer id_customer = resultSet.getInt("ma_khach_hang");
   System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", id, invoice, delivery, pay, delivered, id_customer);
  }
  while (true) {
   System.out.println("\n1. Xem chi tiết hóa đơn");
   System.out.println("2. Danh sách hóa đơn");
   System.out.println("3. Trang chủ");
   System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

   String command = scanner.nextLine();
   switch (command) {
    case "1":
     System.out.print("Nhập mã hóa đơn mà bạn muốn xem:");
     int ma_hoa_don = scanner.nextInt();
     xem_tt_hd(ma_hoa_don);
     break;
    case "2":
     ds_hoa_don();
     break;
    case "3":
     trang_chu();
     break;
    default:
     System.out.println("Lệnh không hợp lệ.");
     break;
   }
  }
 }

 private void xem_tt_hd(int ma_hoa_don) throws SQLException {
  Scanner scanner = new Scanner(System.in);

  int ket_qua;
  Integer giao_hang = null;

  System.out.println("-".repeat(100));

  System.out.println("Thông tin hóa đơn\n");

  CallableStatement statement = database.connection.prepareCall("{call tt_hoa_don(?, ?)}");
  statement.setInt(1, ma_hoa_don);
  statement.registerOutParameter(2, Types.INTEGER);
  statement.execute();

  CallableStatement statement2 = database.connection.prepareCall("{call ds_san_pham(?, ?)}");
  statement2.setInt(1, ma_hoa_don);
  statement2.registerOutParameter(2, Types.INTEGER);
  statement2.execute();

  ket_qua = statement.getInt(2);

  if (ket_qua == 1) {

   ResultSet resultSet = statement.getResultSet();
   ResultSet resultSet2 = statement2.getResultSet();

   System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Ngày lập", "Ngày giao", "Thanh toán", "Giao hàng", "Tên khách hàng", "Địa chỉ giao hàng", "Số điện thoại");
   while (resultSet.next()) {
    Date ngay_lap = resultSet.getDate("ngay_lap");
    Date ngay_giao = resultSet.getDate("ngay_giao");
    Integer thanh_toan = resultSet.getInt("thanh_toan");
    giao_hang = resultSet.getInt("giao_hang");
    String ten_khach_hang = resultSet.getString("ten_khach_hang");
    String dia_chi = resultSet.getString("dia_chi_giao_hang");
    String sdt = resultSet.getString("sdt");
    System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", ngay_lap, ngay_giao, thanh_toan, giao_hang, ten_khach_hang, dia_chi, sdt);
   }

   System.out.format("\n%-20s %-20s %-20s %-20s%n", "Tên sản phẩm", "Giá sản phẩm", "Số lượng", "Đơn giá");
   while (resultSet2.next()) {
    String ten_san_pham = resultSet2.getString("ten_san_pham");
    int gia_san_pham = resultSet2.getInt("gia_san_pham");
    int so_luong = resultSet2.getInt("so_luong");
    int don_gia = resultSet2.getInt("don_gia");
    System.out.format("%-20s %-20s %-20s %-20s%n", ten_san_pham, gia_san_pham, so_luong, don_gia);
   }

   while (true) {
    if (giao_hang == 1) {
     System.out.println("1. Trang chủ");
     System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

     String command = scanner.nextLine();
     switch (command) {
      case "1":
       trang_chu();
       break;
      default:
       System.out.println("Lệnh không hợp lệ.");
       break;
     }
    } else {
     System.out.println("\n1. Giao hàng");
     System.out.println("2. Danh sách hóa đơn");
     System.out.println("3. Trang chủ");
     System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

     String command = scanner.nextLine();
     switch (command) {
      case "1":
       giao_hang(ma_hoa_don);
       break;
      case "2":
       ds_hoa_don();
       break;
      case "3":
       trang_chu();
       break;
      default:
       System.out.println("Lệnh không hợp lệ.");
       break;
     }
    }
   }
  } else {
   System.out.println("Hóa đơn không tồn tại.");
   trang_chu();
  }
 }

 private void giao_hang(int ma_hoa_don) throws SQLException {
  int ket_qua;

  CallableStatement statement = database.connection.prepareCall("{?=call giao_hang(?)}");
  statement.registerOutParameter(1, Types.INTEGER);
  statement.setInt(2, ma_hoa_don);

  statement.execute();

  ket_qua = statement.getInt(1);

  if (ket_qua == 1) {
   System.out.println("Giao hàng thành công.");
   xem_tt_hd(ma_hoa_don);
  } else if (ket_qua == -1) {
   System.out.println("Đơn hàng đã được giao.");
  } else if (ket_qua == -2) {
   System.out.println("Hóa đơn không tồn tại");
  } else {
   System.out.println("Hóa đơn chưa thanh toán.");
  }
 }

 private void ds_san_pham(String ten_san_pham) throws SQLException {
  Scanner scanner = new Scanner(System.in);
  Integer id = null;

  System.out.println("-".repeat(100));

  System.out.println("Danh sách sản phẩm\n");

  CallableStatement statement = database.connection.prepareCall("{call tim_san_pham(?)}");
  statement.setString(1, ten_san_pham);
  statement.execute();

  ResultSet resultSet = statement.getResultSet();

  System.out.format("%-20s %-20s %-20s%n", "Mã sản phẩm", "Tên sản phẩm", "Giá sản phẩm");
  while (resultSet.next()) {
   id = resultSet.getInt("ma_san_pham");
   String name = resultSet.getString("ten_san_pham");
   String price = resultSet.getString("gia_san_pham");
   System.out.format("%-20s %-20s %-20s%n", id, name, price);
  }
  while (true) {
   System.out.println("\n1. Tìm kiếm sản phẩm");
   System.out.println("2. Chi tiết sản phẩm");
   System.out.println("3. Thêm sản phẩm");
   System.out.println("4. Sửa sản phẩm");
   System.out.println("5. Xóa sản phẩm");
   System.out.println("6. Trang chủ");
   System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

   String command = scanner.nextLine();
   switch (command) {
    case "1":
     System.out.print("Nhập tên sản phẩm bạn muốn tìm: ");
     String name = scanner.nextLine();
     ds_san_pham(name);
     break;
    case "2":
     System.out.print("Nhập mã sản phẩm: ");
     id = scanner.nextInt();
     ct_san_pham(id);
     break;
    case "3":
     them_san_pham();
     break;
    case "4":
     System.out.print("Nhập mã sản phẩm: ");
     id = scanner.nextInt();
     sua_san_pham(id);
     break;
    case "5":
     System.out.print("Nhập mã sản phẩm: ");
     id = scanner.nextInt();
     xoa_san_pham(id);
     break;
    case "6":
     trang_chu();
     break;
    default:
     System.out.println("Lệnh không hợp lệ.");
     break;
   }
  }
 }

 private void ct_san_pham(Integer id) throws SQLException {
  Scanner scanner = new Scanner(System.in);
  int ket_qua;
  System.out.println("-".repeat(100));

  System.out.println("Thông tin sản phẩm\n");

  CallableStatement statement = database.connection.prepareCall("{call tt_san_pham(?, ?)}");
  statement.setInt(1, id);
  statement.registerOutParameter(2, Types.INTEGER);
  statement.execute();

  ket_qua = statement.getInt(2);

  if (ket_qua == 1) {
   ResultSet resultSet = statement.getResultSet();

   System.out.format("%-20s %-20s %-20s %-20s %-20s%n", "Mã sản phẩm", "Tên sản phẩm", "Mô tả", "Giá sản phẩm", "Tồn kho");
   while (resultSet.next()) {
    id = resultSet.getInt("ma_san_pham");
    String name = resultSet.getString("ten_san_pham");
    String descripe = resultSet.getString("mo_ta");
    String price = resultSet.getString("gia_san_pham");
    String stock = resultSet.getString("ton_kho");
    System.out.format("%-20s %-20s %-20s %-20s %-20s%n", id, name, descripe, price, stock);
   }
   while (true) {
    System.out.println("\n1. Danh sách sản phẩm");
    System.out.println("2. Thêm sản phẩm vào giỏ hàng");
    System.out.println("3. Trang chủ");
    System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

    String command = scanner.nextLine();
    switch (command) {
     case "1":
      ds_san_pham("");
      break;
     case "2":
      trang_chu();
      break;
     default:
      System.out.println("Lệnh không hợp lệ.");
      break;
    }
   }
  }
 }

 private void them_san_pham() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  while (true) {

   System.out.println("-".repeat(100));

   System.out.println("Thêm sản phẩm\n");

   System.out.print("Nhập tên sản phẩm: ");
   String name = scanner.next();

   System.out.print("Nhập mô tả sản phẩm: ");
   String descripe = scanner.next();
   scanner.nextLine();

   System.out.print("Nhập giá sản phẩm: ");
   Integer price = scanner.nextInt();
   scanner.nextLine();

   System.out.print("Nhập số lượng sản phẩm trong kho: ");
   Integer stock = scanner.nextInt();
   scanner.nextLine();

   CallableStatement statement = database.connection.prepareCall("{ ?=call them_san_pham(?,?,?,?)}");
   statement.registerOutParameter(1, Types.BOOLEAN);
   statement.setString(2, name);
   statement.setInt(3, price);
   statement.setString(4, descripe);
   statement.setInt(5, stock);
   statement.execute();

   if (!statement.getBoolean(1))
    System.out.println("Sản phẩm đã tồn tại");
   else {
    System.out.println("Thêm sản phẩm thành công");
    break;
   }
  }
  ds_san_pham("");
 }

 private void sua_san_pham(int ma_san_pham) throws SQLException {

  Scanner scanner = new Scanner(System.in);
  int ket_qua;
  String name, descripe;
  Integer price, stock;
  String name_old = null, descripe_old = null;
  Integer price_old = null, stock_old = null;

  CallableStatement statement = database.connection.prepareCall("{call tt_san_pham(?, ?)}");
  statement.setInt(1, ma_san_pham);
  statement.registerOutParameter(2, Types.INTEGER);
  statement.execute();

  ket_qua = statement.getInt(2);

  if (ket_qua == 1) {
   ResultSet resultSet = statement.getResultSet();
   while (resultSet.next()) {
    name_old = resultSet.getString("ten_san_pham");
    descripe_old = resultSet.getString("mo_ta");
    price_old = resultSet.getInt("gia_san_pham");
    stock_old = resultSet.getInt("ton_kho");
   }
   while (true) {
    System.out.println("-".repeat(100));

    System.out.print("Nhập tên sản phẩm: ");
    name = scanner.nextLine();

    System.out.print("Nhập mô tả sản phẩm: ");
    descripe = scanner.next();

    System.out.print("Nhập giá sản phẩm: ");
    price = scanner.nextInt();
    scanner.nextLine();

    System.out.print("Nhập số lượng sản phẩm trong kho: ");
    stock = scanner.nextInt();
    scanner.nextLine();

    name = name.isEmpty() ? name_old : name;
    descripe = descripe.isEmpty() ? descripe_old : descripe;
    price = (price == null) ? price_old : price;
    stock = (stock == null) ? stock_old : stock;

    statement = database.connection.prepareCall("{?=call sua_san_pham(?,?,?,?,?)}");
    statement.registerOutParameter(1, Types.INTEGER);
    statement.setInt(2, ma_san_pham);
    statement.setString(3, name);
    statement.setString(4, descripe);
    statement.setInt(5, price);
    statement.setInt(6, stock);
    statement.execute();

    if (statement.getInt(1) == 0) {
     System.out.println("Sản phẩm đã tồn tại");
    } else {
     System.out.println("Sửa thông tin sản phẩm thành công.");
     break;
    }
   }
  } else {
   System.out.println("Sản phẩm không tồn tại");
  }
  ds_san_pham("");
 }

 private void xoa_san_pham(int ma_san_pham) throws SQLException {

  Scanner scanner = new Scanner(System.in);
  int ket_qua;

  CallableStatement statement = database.connection.prepareCall("{?=call xoa_san_pham(?)}");
  statement.registerOutParameter(1, Types.INTEGER);
  statement.setInt(2, ma_san_pham);
  statement.execute();

  ket_qua = statement.getInt(1);

  if (ket_qua == 1) {
   System.out.println("Xóa sản phẩm thành công");
  } else {
   System.out.println("Sản phẩm không tồn tại");
  }
  ds_san_pham("");
 }

}
