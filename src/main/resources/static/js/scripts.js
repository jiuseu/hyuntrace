/*!
* Start Bootstrap - Simple Sidebar v6.0.5 (https://startbootstrap.com/template/simple-sidebar)
* Copyright 2013-2022 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-simple-sidebar/blob/master/LICENSE)
*/
// 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

     // 현재 페이지 URL 가져오기
      const currentPath = window.location.pathname;

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

     // 로그인/로그아웃 버튼 동작 처리

      const loginBtn = document.getElementById('loginBtn');
      const logoutBtn = document.getElementById('logoutBtn');
      const loginNavItem = document.getElementById('loginNavItem');
      const userDropdown = document.getElementById('userDropdown');
      const userId = document.getElementById('userId');
      const viewInfoBtn = document.getElementById('viewInfoBtn');
      const registerBtn = document.querySelector('.registerBtn')

     if (currentPath !== "/member/join") {
       fetch('/api/member/info')
              .then(response => response.json())
              .then(data => {
                if (data.mid) {
                 console.log("로그인성공")
                  loginNavItem.style.display = 'none';
                  userDropdown.style.display = 'block';
                  registerBtn.style.display = 'none';
                  userId.textContent = data.mid // 실제 사용자 아이디로 업데이트
                } else {
                console.log("로그인상태 아님")
                  loginNavItem.style.display = 'block';
                  userDropdown.style.display = 'none';
                  registerBtn.style.display = 'block';
                }
              });

         }
      // 로그인 버튼 클릭 이벤트
      loginBtn.addEventListener('click', function() {

      console.log("로그인 클릭");
      self.location = `/member/login` // 로그인 페이지로 리디렉션

      }); //로그인 버튼 끝

      // 로그아웃 버튼 클릭 이벤트
      logoutBtn.addEventListener('click', function() {

      self.location = `/logout`
      console.log("로그아웃 클릭");

      });

      // 내 정보 조회 버튼 클릭 이벤트
      viewInfoBtn.addEventListener('click', function() {
      console.log("내 정보 조회 클릭");
      // 내 정보 조회 처리 (예: 모달 띄우기, 페이지 이동 등)
      self.location = `/member/info` //
      });

});
