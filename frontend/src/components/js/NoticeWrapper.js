import React from "react";
import "../css/NoticeWrapper.css";

const NoticeWrapper = ({ children }) => {
  return (
    <>
      <div className='NoticeWrapper'>{children}</div>
    </>
  );
};

export default NoticeWrapper;
