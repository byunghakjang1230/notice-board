import React from "react";
import "../css/Pagination.css";
import _ from "lodash";
import "bootstrap/dist/css/bootstrap.min.css";

function getStartPage(currentPage, pageSize) {
  return parseInt(currentPage / pageSize) * pageSize + 1;
}

function getEndPage(startPage, pageSize, totalPages) {
  return startPage + pageSize > totalPages
    ? totalPages + 1
    : startPage + pageSize;
}

const Pagination = (props) => {
  const { totalPages, currentPage, pageSize, onPageChange } = props;
  const startPage = getStartPage(currentPage, pageSize);
  const endPage = getEndPage(startPage, pageSize, totalPages);
  const pages = _.range(startPage, endPage);
  const map = new Map();
  const isFirstPage = startPage === 1 ? true : false;
  const isLastPage = endPage === totalPages + 1;
  pages.map((page) => {
    map.set(page, page === currentPage ? "active" : "");
  });

  const getPageList = (page) => {
    const fixedPage = page - 1;
    return (
      <li
        className={"page-item" + (fixedPage === currentPage ? " active" : "")}
        onClick={() => onPageChange(fixedPage)}
      >
        <a className='page-link' href='#'>
          {page}
        </a>
      </li>
    );
  };

  return (
    <nav aria-label='Page navigation example' className='Pagination'>
      <ul className='pagination justify-content-center'>
        <li
          className={"page-item" + (isFirstPage ? " disabled" : "")}
          onClick={isFirstPage ? () => {} : () => onPageChange(startPage - 2)}
        >
          <a
            className='page-link'
            href='#'
            aria-label='Previous'
            aria-disabled={false}
          >
            <span aria-hidden='true'>&laquo;</span>
          </a>
        </li>
        {pages.map((page) => {
          return getPageList(page);
        })}
        <li
          className={"page-item" + (isLastPage ? " disabled" : "")}
          onClick={isLastPage ? () => {} : () => onPageChange(endPage - 1)}
        >
          <a
            className='page-link'
            href='#'
            aria-label='Next'
            aria-disabled={false}
          >
            <span aria-hidden='true'>&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  );
  // }
};

export default Pagination;
