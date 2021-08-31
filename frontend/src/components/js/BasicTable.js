import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

export default function BasicTable({ history, data }) {
  const classes = useStyles();
  const callback = (id) => {
    history.push("/notice/" + id);
  };

  return (
    <TableContainer component={Paper}>
      <Table className={classes.table} aria-label='customized table'>
        <TableHead>
          <TableRow>
            <TableCell size='small'>| No</TableCell>
            <TableCell align='left'>| Writer</TableCell>
            <TableCell align='left'>| Title</TableCell>
            <TableCell align='left'>| Create DateTime</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((row) => (
            <TableRow
              key={row.id}
              onClick={() => callback(row.id)}
              hover={true}
            >
              <TableCell component='th' scope='row'>
                {row.id}
              </TableCell>
              <TableCell align='left'>{row.writer}</TableCell>
              <TableCell align='left'>{row.title}</TableCell>
              <TableCell align='left'>{row.createDateTime}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
