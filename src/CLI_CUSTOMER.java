
import java.sql.*;
import java.util.Scanner;

public class CLI_CUSTOMER {
 private Database database;
 private int ma_khach_hang;
 private int[][] gio_hang = new int[100][2];
 private String[] ten_sp_gh = new String[100];
 private int[] gia_sp_gh = new int[100];
 private int tong_gio_hang;

 public CLI_CUSTOMER(Database database) {
  this.database = database;
  this.ma_khach_hang = 0;
  this.tong_gio_hang = 0;
 }

 public void run() throws SQLException {
  trang_chu();
 }

 private void trang_chu() throws SQLException {
  while (true) {
   System.out.println("-".repeat(100));
   System.out.println("Trang Chủ\n");
   if (ma_khach_hang == 0) {

    System.out.println("1. Đăng nhập");
    System.out.println("2. Đăng ký");
    System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

    Scanner scanner = new Scanner(System.in);
    String command = scanner.nextLine();

    switch (command) {
     case "1":
      dang_nhap();
      break;
     case "2":
      dang_ky();
      break;
     default:
      System.out.println("Lệnh không hợp lệ.");
      break;
    }
   } else {
    System.out.println("1. Xem danh sách sản phẩm");
    System.out.println("2. Xem thông tin khách hàng");
    System.out.println("3. Xem giỏ hàng");
    System.out.println("4. Đăng xuất");
    System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

    Scanner scanner = new Scanner(System.in);
    String command = scanner.nextLine();

    switch (command) {
     case "1":
      ds_san_pham("");
      break;
     case "2":
      xem_tt_kh();
      break;
     case "3":
      xem_gio_hang();
      break;
     case "4":
      dang_xuat();
      break;
     default:
      System.out.println("Lệnh không hợp lệ.");
      break;
    }
   }
  }
 }

 private void dang_nhap() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  while (this.ma_khach_hang >= 0) {

   System.out.println("-".repeat(100));

   System.out.println("Đăng nhập\n");

   System.out.print("Nhập email: ");
   String email = scanner.nextLine();

   System.out.print("Nhập mật khẩu: ");
   String pass = scanner.nextLine();

   CallableStatement statement = database.connection.prepareCall("{ ?=call dang_nhap(?,?)}");
   statement.registerOutParameter(1, Types.BIGINT);
   statement.setString(2, email);
   statement.setString(3, pass);
   statement.execute();

   if (statement.getInt(1) == -1)
    System.out.println("Email không hợp lệ");
   else if (statement.getInt(1) == -2) {
    System.out.println("Mật khâu không hợp lệ");
   } else {
    System.out.println("Đăng nhập thành công");
    this.ma_khach_hang = statement.getInt(1);
    break;
   }
  }
 }

 private void dang_ky() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  while (this.ma_khach_hang >= 0) {

   System.out.println("-".repeat(100));

   System.out.println("Đăng ký\n");

   System.out.print("Nhập tên khách hàng: ");
   String name = scanner.nextLine();

   System.out.print("Nhập email: ");
   String email = scanner.nextLine();

   System.out.print("Nhập mật khẩu: ");
   String pass = scanner.nextLine();

   System.out.print("Nhập lại mật khẩu: ");
   String repass = scanner.nextLine();

   if (!repass.equals(pass)) {
    System.out.println("Mật khẩu nhập lại không khớp");
   } else {
    CallableStatement statement = database.connection.prepareCall("{ ?=call dang_ky(?,?,?)}");
    statement.registerOutParameter(1, Types.BOOLEAN);
    statement.setString(2, email);
    statement.setString(3, pass);
    statement.setString(4, name);
    statement.execute();

    if (!statement.getBoolean(1))
     System.out.println("Email đã tồn tại");
    else {
     System.out.println("Đăng ký thành công");
     break;
    }
   }

  }
 }

 private void dang_xuat() throws SQLException {
  this.ma_khach_hang = 0;
 }

 private void xem_tt_kh() throws SQLException {
  Scanner scanner = new Scanner(System.in);

  int ket_qua;

  System.out.println("-".repeat(100));

  System.out.println("Thông tin khách hàng\n");

  CallableStatement statement = database.connection.prepareCall("{call tt_khach_hang(?, ?)}");
  statement.setInt(1, this.ma_khach_hang);
  statement.registerOutParameter(2, Types.INTEGER);
  statement.execute();

  ket_qua = statement.getInt(2);

  if (ket_qua == 1) {
   ResultSet resultSet = statement.getResultSet();
   System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", "Mã khách hàng", "Địa chỉ email", "Tên khách hàng", "Giới tính", "Địa chỉ giao hàng", "Số điện thoại");
   while (resultSet.next()) {
    int id = resultSet.getInt("ma_khach_hang");
    String email = resultSet.getString("dia_chi_email");
    String name = resultSet.getString("ten_khach_hang");
    String sex = resultSet.getString("gioi_tinh");
    String address = resultSet.getString("dia_chi_giao_hang");
    String phone = resultSet.getString("sdt");
    System.out.format("\n%-20s %-20s %-20s %-20s %-20s %-20s%n", id, email, name, sex, address, phone);
   }
   while (true) {
    System.out.println("\n1. Xem danh sách hóa đơn");
    System.out.println("2. Sửa thông tin khách hàng");
    System.out.println("3. Xem danh sách sản phẩm");
    System.out.println("4. Xem giỏ hàng");
    System.out.println("5. Trang chủ");
    System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

    String command = scanner.nextLine();
    switch (command) {
     case "1":
      ds_hoa_don();
      break;
     case "2":
      sua_tt_kh();
      xem_tt_kh();
      break;
     case "3":
      ds_san_pham("");
      break;
     case "4":
      xem_gio_hang();
      break;
     case "5":
      trang_chu();
      break;
     default:
      System.out.println("Lệnh không hợp lệ.");
      break;
    }
   }
  } else {
   System.out.println("Tài khoản không tồn tại.");
   trang_chu();
  }
 }

 private void sua_tt_kh() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  int ket_qua;
  String email, name, sex, address, phone, pass, repass;
  String email_old = null, name_old = null, sex_old = null, address_old = null, phone_old = null, pass_old = null;

  CallableStatement statement = database.connection.prepareCall("{call tt_khach_hang(?, ?)}");
  statement.setInt(1, this.ma_khach_hang);
  statement.registerOutParameter(2, Types.INTEGER);
  statement.execute();

  ket_qua = statement.getInt(2);

  if (ket_qua == 1) {
   ResultSet resultSet = statement.getResultSet();
   while (resultSet.next()) {
    email_old = resultSet.getString("dia_chi_email");
    name_old = resultSet.getString("ten_khach_hang");
    sex_old = resultSet.getString("gioi_tinh");
    address_old = resultSet.getString("dia_chi_giao_hang");
    phone_old = resultSet.getString("sdt");
    pass_old = resultSet.getString("mat_khau");
   }
   while (true) {
    System.out.println("-".repeat(100));

    System.out.println("Sửa thông tin khách hàng\n");
    System.out.print("Nhập email mới: ");
    email = scanner.nextLine();
    System.out.print("Nhập tên khách hàng mới: ");
    name = scanner.nextLine();
    System.out.print("Nhập giới tính mới: ");
    sex = scanner.nextLine();
    System.out.print("Nhập địa chỉ giao hàng mới: ");
    address = scanner.nextLine();
    System.out.print("Nhập số điện thoại mới: ");
    phone = scanner.nextLine();
    System.out.print("Nhập mật khẩu mới: ");
    pass = scanner.nextLine();
    System.out.print("Nhập lại mật khẩu mới: ");
    repass = scanner.nextLine();

    email = email.isEmpty() ? email_old : email;
    name = name.isEmpty() ? name_old : name;
    sex = sex.isEmpty() ? sex_old : sex;
    address = address.isEmpty() ? address_old : address;
    phone = phone.isEmpty() ? phone_old : phone;
    pass = pass.isEmpty() ? pass_old : pass;

    if (!pass.equals(pass_old) && !pass.equals(repass)) {
     System.out.println("Mật khẩu nhập lại không khớp.");
    } else {
     statement = database.connection.prepareCall("{?=call cap_nhat_tt(?,?,?,?,?,?,?)}");
     statement.registerOutParameter(1, Types.INTEGER);
     statement.setInt(2, this.ma_khach_hang);
     statement.setString(3, email);
     statement.setString(4, pass);
     statement.setString(5, name);
     statement.setString(6, sex);
     statement.setString(7, address);
     statement.setString(8, phone);
     statement.execute();

     if (statement.getInt(1) == 0) {
      System.out.println("Email đã tồn tại");
     } else {
      System.out.println("Sửa thông tin khách hàng thành công.");
      break;
     }
    }
   }
  } else {
   System.out.println("Tài khoản không tồn tại");
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
   System.out.println("2. Thêm sản phẩm vào giỏ hàng");
   System.out.println("3. Chi tiết sản phẩm");
   System.out.println("4. Trang chủ");
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
     System.out.print("Nhập số lượng sản phẩm: ");
     Integer quantity = scanner.nextInt();
     them_gio_hang(id, quantity);
     break;
    case "3":
     System.out.print("Nhập mã sản phẩm: ");
     id = scanner.nextInt();
     ct_san_pham(id);
     break;
    case "4":
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
      System.out.print("Nhập mã sản phẩm: ");
      id = scanner.nextInt();
      System.out.print("Nhập số lượng sản phẩm: ");
      Integer quantity = scanner.nextInt();
      them_gio_hang(id, quantity);
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
 }

 private void them_gio_hang(Integer id, Integer quantity) throws SQLException {
  Integer valid = null;
  Integer stock = null;
  String name = null;
  Integer price = null;
  int contain = -1;

  System.out.println("-".repeat(100));

  for (int i = 0; i < this.tong_gio_hang; i++) {
   if (this.gio_hang[i][0] == id) {
    contain = i;
   }
  }

  if (contain != -1) {
   this.gio_hang[contain][1] = this.gio_hang[contain][1] + quantity;
  } else {


   CallableStatement statement = database.connection.prepareCall("{call tt_san_pham(?, ?)}");
   statement.setInt(1, id);
   statement.registerOutParameter(2, Types.INTEGER);
   statement.execute();

   valid = statement.getInt(2);

   if (valid == 0) {
    System.out.println("Sản phẩm không tồn tại.");
   } else {
    ResultSet resultSet = statement.getResultSet();
    while (resultSet.next()) {
     stock = resultSet.getInt("ton_kho");
     name = resultSet.getString("ten_san_pham");
     price = resultSet.getInt("gia_san_pham");
    }
    if (stock < quantity) {
     System.out.println("Không đủ số lượng sản phẩm.");
    } else {
     this.gio_hang[this.tong_gio_hang][0] = id;
     this.gio_hang[this.tong_gio_hang][1] = quantity;
     this.ten_sp_gh[this.tong_gio_hang] = name;
     this.gia_sp_gh[this.tong_gio_hang] = quantity * price;
     this.tong_gio_hang++;
     System.out.println("Thêm sản phẩm vào giỏ hàng thành công.");
     xem_gio_hang();
    }
   }
  }
 }

 private void xem_gio_hang() throws SQLException {
  Scanner scanner = new Scanner(System.in);

  System.out.println("-".repeat(100));

  System.out.println("Giỏ hàng");

  System.out.format("%-20s %-20s %-20s %-20s%n", "STT", "Tên sản phẩm", "Số lượng", "Đơn giá");
  for (int i = 0; i < this.tong_gio_hang; i++) {
   String name = this.ten_sp_gh[i];
   int quantity = this.gio_hang[i][1];
   int price = this.gia_sp_gh[i];
   System.out.format("%-20s %-20s %-20s %-20s%n", i, name, quantity, price);
  }
  while (true) {
   System.out.println("\n1. Danh sách sản phẩm");
   System.out.println("2. Xóa sản phẩm ra khỏi giỏ hàng");
   System.out.println("3. Lặp hóa đơn");
   System.out.println("4. Trang chủ");
   System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

   String command = scanner.nextLine();
   switch (command) {
    case "1":
     ds_san_pham("");
     break;
    case "2":
     System.out.print("Nhập số thứ tự sản phẩm trong giỏ hàng: ");
     int stt = scanner.nextInt();
     xoa_gio_hang(stt);
     break;
    case "3":
     lap_hoa_don();
     break;
    case "4":
     trang_chu();
     break;
    default:
     System.out.println("Lệnh không hợp lệ.");
     break;
   }
  }
 }

 private void xoa_gio_hang(int stt) throws SQLException {

  if (stt < 0 || stt > this.tong_gio_hang - 1) {
   System.out.println("Số thứ tự không hợp lệ.");
  } else {
   for (int i = stt; i < this.tong_gio_hang - stt; i++) {
    this.gio_hang[i][0] = this.gio_hang[i + 1][0];
    this.gio_hang[i][1] = this.gio_hang[i + 1][1];
    this.gia_sp_gh[i] = this.gia_sp_gh[i + 1];
    this.ten_sp_gh[i] = this.ten_sp_gh[i + 1];
   }
   this.tong_gio_hang--;
   System.out.println("Xóa thành công.");
   xem_gio_hang();
  }
 }

 private void lap_hoa_don() throws SQLException {
  boolean flag = false;

  CallableStatement statement = database.connection.prepareCall("{?=call tao_hoa_don(?)}");
  statement.registerOutParameter(1, Types.BIGINT);
  statement.setInt(2,this.ma_khach_hang);
  statement.execute();

  int ma_hoa_don = statement.getInt(1);

  if (ma_hoa_don != -1) {
   for (int i = 0; i < this.tong_gio_hang; i++) {

    statement = database.connection.prepareCall("{?=call them_ct_hd(?,?,?)}");
    statement.registerOutParameter(1, Types.BIGINT);
    statement.setInt(2,ma_hoa_don);
    statement.setInt(3,this.gio_hang[i][0]);
    statement.setInt(4,this.gio_hang[i][1]);
    statement.execute();

    if (statement.getInt(1) != 1){
     flag = true;
     break;
    }

    this.gio_hang[i] = null;
    this.gia_sp_gh[i] = 0;
    this.ten_sp_gh[i] = null;
   }

   this.tong_gio_hang = 0;

   if (!flag) {
    System.out.println("Lặp hóa đơn thành công.");
    xem_tt_hd(ma_hoa_don);
   } else {
    System.out.println("Lặp hóa đơn không thành công.");
   }

  } else {
   System.out.println("Tài khoản không tồn tại.");
  }
 }

 private void xem_tt_hd(int ma_hoa_don) throws SQLException {
  Scanner scanner = new Scanner(System.in);

  int ket_qua;
  Integer thanh_toan = null;

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

   System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n","Ngày lập", "Ngày giao", "Thanh toán", "Giao hàng", "Tên khách hàng", "Địa chỉ giao hàng", "Số điện thoại");
   while (resultSet.next()) {
    Date ngay_lap = resultSet.getDate("ngay_lap");
    Date ngay_giao = resultSet.getDate("ngay_giao");
    thanh_toan = resultSet.getInt("thanh_toan");
    Integer giao_hang = resultSet.getInt("giao_hang");
    String ten_khach_hang = resultSet.getString("ten_khach_hang");
    String dia_chi = resultSet.getString("dia_chi_giao_hang");
    String sdt = resultSet.getString("sdt");
    System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", ngay_lap, ngay_giao, thanh_toan, giao_hang, ten_khach_hang, dia_chi, sdt);
   }

   System.out.format("\n%-20s %-20s %-20s %-20s%n","Tên sản phẩm", "Giá sản phẩm", "Số lượng", "Đơn giá");
   while (resultSet2.next()) {
    String ten_san_pham = resultSet2.getString("ten_san_pham");
    int gia_san_pham = resultSet2.getInt("gia_san_pham");
    int so_luong = resultSet2.getInt("so_luong");
    int don_gia = resultSet2.getInt("don_gia");
    System.out.format("%-20s %-20s %-20s %-20s%n", ten_san_pham, gia_san_pham, so_luong, don_gia);
   }

   while (true) {
    if (thanh_toan == 1) {
     System.out.println("\n1. Xem thông tin khách hàng");
     System.out.println("2. Xem danh sách hóa đơn");
     System.out.println("3. Trang chủ");
     System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

     String command = scanner.nextLine();
     switch (command) {
      case "1":
       xem_tt_kh();
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
    } else {


     System.out.println("\n1. Thanh toán");
     System.out.println("2. Trang chủ");
     System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

     String command = scanner.nextLine();
     switch (command) {
      case "1":
       thanh_toan(ma_hoa_don);
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
  } else {
   System.out.println("Hóa đơn không tồn tại.");
   trang_chu();
  }
 }

 private void thanh_toan(int ma_hoa_don) throws SQLException {
  int ket_qua;

  CallableStatement statement = database.connection.prepareCall("{?=call thanh_toan(?)}");
  statement.registerOutParameter(1, Types.INTEGER);
  statement.setInt(2, ma_hoa_don);

  statement.execute();

  ket_qua = statement.getInt(1);

  if (ket_qua == 1) {
   System.out.println("Thanh toán thành công.");
   xem_tt_hd(ma_hoa_don);
  } else {
   System.out.println("Hóa đơn không tồn tại.");
   trang_chu();
  }
 }

 private void ds_hoa_don() throws SQLException {
  Scanner scanner = new Scanner(System.in);
  int ket_qua;

  System.out.println("-".repeat(100));

  System.out.println("Danh sách hóa đơn\n");

  CallableStatement statement = database.connection.prepareCall("{call ds_hoa_don(?,?)}");
  statement.setInt(1, this.ma_khach_hang);
  statement.registerOutParameter(2, Types.INTEGER);
  statement.execute();

  ket_qua = statement.getInt(2);
  if (ket_qua == 1) {
   ResultSet resultSet = statement.getResultSet();

   System.out.format("%-20s %-20s %-20s %-20s %-20s%n", "Mã hóa đơn", "Ngày lập", "Ngày giao", "Thanh toán", "Giao hàng");
   while (resultSet.next()) {
    Integer id = resultSet.getInt("ma_hoa_don");
    Date invoice = resultSet.getDate("ngay_lap");
    Date delivery = resultSet.getDate("ngay_giao");
    int pay = resultSet.getInt("thanh_toan");
    int delivered = resultSet.getInt("giao_hang");
    System.out.format("%-20s %-20s %-20s %-20s %-20s%n", id, invoice, delivery, pay, delivered);
   }
   while (true) {
    System.out.println("\n1. Xem chi tiết hóa đơn");
    System.out.println("2. Danh sách sản phẩm");
    System.out.println("3. Giỏ hàng");
    System.out.println("3. Xem thông tin khách hàng");
    System.out.println("4. Trang chủ");
    System.out.print("\nNhập số tương ứng với lựa chọn của bạn: ");

    String command = scanner.nextLine();
    switch (command) {
     case "1":
      System.out.print("Nhập mã hóa đơn mà bạn muốn xem:");
      int ma_hoa_don = scanner.nextInt();
      xem_tt_hd(ma_hoa_don);
      break;
     case "2":
      ds_san_pham("");
      break;
     case "3":
      xem_gio_hang();
      break;
     case "4":
      xem_tt_kh();
      break;
     case "5":
      trang_chu();
      break;
     default:
      System.out.println("Lệnh không hợp lệ.");
      break;
    }
   }
  } else {
   System.out.println("Hóa đơn không tồn tại.");
  }
 }

}