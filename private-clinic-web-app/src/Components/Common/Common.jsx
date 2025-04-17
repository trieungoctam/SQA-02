import { Alert, Snackbar } from "@mui/material";

export const CustomerSnackbar = ({ open, message, severity }) => {
  return (
    <Snackbar
      anchorOrigin={{ vertical: "top", horizontal: "center" }}
      open={open}
      key={"top center"}
    >
      <Alert severity={severity} variant="filled" sx={{ width: "100%" }}>
        {message}
      </Alert>
    </Snackbar>
  );
};

export const isBENHNHAN = (currentUser) => {
  if (currentUser.role.name === "ROLE_BENHNHAN") return true;
  return false;
};

export const isYTA = (currentUser) => {
  if (currentUser.role.name === "ROLE_YTA") return true;
  return false;
};

export const isBACSI = (currentUser) => {
  if (currentUser.role.name === "ROLE_BACSI") return true;
  return false;
};

export const isTUVAN = (currentUser) => {
  if (currentUser.role.name === "ROLE_TUVAN") return true;
  return false;
};
