import axios from "axios";
import * as cookie from "../utils/Cookies";

const defaultUrl = "http://localhost:3000/api";

export function getMethod(path) {
  const headers = getHeader();
  return axios.get(defaultUrl + path, { headers });
}

export function postMethod(path, data) {
  const headers = getHeader();
  return axios.post(defaultUrl + path, data, { headers });
}

export function deleteMethod(path) {
  const headers = getHeader();
  return axios.delete(defaultUrl + path, { headers });
}

export function putMethod(path, data) {
  const headers = getHeader();
  return axios.put(defaultUrl + path, data, { headers });
}

function getHeader() {
  const headers = {
    "Content-Type": "application/json",
    Accept: "application/json",
  };
  const auth = cookie.getCookie("Authorization");
  if (auth !== undefined) {
    headers.Authorization = auth;
  }
  return headers;
}

export async function deleteNotice(id) {
  return await deleteMethod("/notices/" + id);
}

export async function updateNotice(id, data) {
  return await putMethod("/notices/" + id, data);
}

export async function saveNotice(data) {
  return await postMethod("/notices", data);
}

export async function saveFiles(data) {
  const headers = {
    "Content-Type": "multipart/form-data",
  };
  const auth = cookie.getCookie("Authorization");
  if (auth !== undefined) {
    headers.Authorization = auth;
  }
  return await axios.post(defaultUrl + "/file", data, { headers });
}

export async function login(data) {
  return await postMethod("/login", data);
}

export async function join(data) {
  return await postMethod("/members", data);
}

export async function findNotice(page) {
  return await getMethod("/notices/" + page);
}

export async function getPagingNotices(page) {
  return await getMethod("/notices?page=" + page + "&size=10");
}
