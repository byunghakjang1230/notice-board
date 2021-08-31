import * as cookie from "../utils/Cookies";

export function isLogin() {
  const auth = cookie.getCookie("Authorization");
  return auth !== undefined && auth.indexOf("Bearer") === 0;
}

export function setLoginToken(token) {
  cookie.setCookie("Authorization", "Bearer " + token, { "max-age": 3600 });
}

export function logOut() {
  cookie.deleteCookie("Authorization", { path: "/" });
}
