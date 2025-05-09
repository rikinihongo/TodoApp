# So sánh Compose Containers và XML ViewGroups

| Compose Container | XML ViewGroup | Mô tả |
|-------------------|---------------|-------|
| **Scaffold** | Không có tương đương trực tiếp, thường là tổ hợp của **`CoordinatorLayout`** + **`AppBarLayout`** + **`DrawerLayout`** | Container cấp cao nhất cho màn hình với topBar, bottomBar, drawer và FAB |
| **Surface** | Không có tương đương trực tiếp, gần nhất là **`CardView`** không có padding | Container với màu nền, độ cao, và hình dạng |
| **Box** | **`FrameLayout`** | Cho phép chồng các phần tử lên nhau |
| **Column** | **`LinearLayout`** (orientation="vertical") | Sắp xếp phần tử theo chiều dọc |
| **Row** | **`LinearLayout`** (orientation="horizontal") | Sắp xếp phần tử theo chiều ngang |
| **Card** | **`CardView`** | Container có độ cao và góc bo tròn |
| **ConstraintLayout** | **`ConstraintLayout`** | Layout phức tạp với các ràng buộc giữa phần tử |
| **LazyColumn** | **`RecyclerView`** (với LinearLayoutManager vertical) | Danh sách có thể cuộn theo chiều dọc |
| **LazyRow** | **`RecyclerView`** (với LinearLayoutManager horizontal) | Danh sách có thể cuộn theo chiều ngang |
| **BottomSheetScaffold** | **`CoordinatorLayout`** + **`BottomSheetBehavior`** | Layout với bottom sheet có thể kéo lên/xuống |
| **ModalBottomSheetLayout** | **`BottomSheetDialogFragment`** | Bottom sheet dạng modal |
| **SwipeToDismiss** | **`ItemTouchHelper`** với **`RecyclerView`** | Thành phần có thể vuốt để xóa |
| **AlertDialog** | **`AlertDialog`** | Hộp thoại thông báo |
| **FlowRow** / **FlowColumn** | **`FlexboxLayout`** | Sắp xếp các phần tử có thể xuống dòng khi không đủ không gian |
| **Grid** | **`GridLayout`** | Bố cục dạng lưới |
| **LazyVerticalGrid** | **`RecyclerView`** với **`GridLayoutManager`** | Lưới có thể cuộn |
| **Pager** | **`ViewPager2`** | Hiển thị các trang có thể vuốt ngang |
| **TabRow** | **`TabLayout`** | Hiển thị các tab |
| **HorizontalPager** | **`ViewPager2`** với orientation horizontal | Container cho các trang có thể vuốt ngang |
| **VerticalPager** | **`ViewPager2`** với orientation vertical | Container cho các trang có thể vuốt dọc |
| **DrawerLayout** | **`DrawerLayout`** | Layout có ngăn kéo (drawer) bên cạnh |
| **BottomNavigation** | **`BottomNavigationView`** | Thanh điều hướng phía dưới |