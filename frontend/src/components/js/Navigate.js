import React, { useState } from "react";
import { Button } from "@material-ui/core";
import "bootstrap/dist/css/bootstrap.min.css";
import "../css/Navigate.css";
import * as loginService from "../../service/login";

const Navigae = () => {
  // const auth = cookie.getCookie("Authorization");
  const [login, setLogin] = useState(loginService.isLogin());

  const onClickLogOut = () => {
    loginService.logOut();
    setLogin(false);
    alert("로그아웃 처리 되었습니다.");
  };

  return (
    <div className='Navigate'>
      <Button variant='outlined' href='/notice' hidden={!login}>
        글쓰기
      </Button>
      <Button variant='outlined' href='/join' hidden={login}>
        회원가입
      </Button>
      <Button variant='outlined' href='/login' hidden={login}>
        로그인
      </Button>
      <Button variant='outlined' onClick={onClickLogOut} hidden={!login}>
        로그아웃
      </Button>
    </div>
  );
};

export default Navigae;
