import { useCallback, useContext, useEffect, useState } from "react";
import "./ExaminationForm.css";
import { CustomerSnackbar, isBACSI } from "../Common/Common";
import { authAPI, endpoints } from "../config/Api";
import { UserContext } from "../config/Context";
import { useLocation, useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import { Typeahead } from "react-bootstrap-typeahead";
import { CircularProgress } from "@mui/material";

export default function ExaminationForm() {
  const [medicineGroupList, setMedicineGroupList] = useState([]);
  const { currentUser, setCurrentUser } = useContext(UserContext);
  const [selectedMGId, setSelectedMGId] = useState(0);
  const [medicinesList, setMedicinesList] = useState([]);
  const [selectedMedicineId, setSelectedMedicineId] = useState(0);
  const [dayExam, setDayExam] = useState(1);
  const [newMedicineOpen, setNewMedicineOpen] = useState(false);
  const [allMedicines, setAllMedicines] = useState([]);

  const [textInputs, setTextInputs] = useState({
    advance: "",
    predict: "",
    symptomProcess: "",
    treatmentProcess: "",
    followUpDate: "",
  });

  const location = useLocation();
  const navigate = useNavigate();

  const { examPatient, precriptionItems, h } = location.state || {};

  const [medicinesExamList, setMedicinesExamList] = useState([]);

  const [loading, setLoading] = useState(false);

  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đăng kí thành công",
    severity: "success",
  });

  const showSnackbar = (message, severity) => {
    setData({
      message: message,
      severity: severity,
    });

    setOpen(true);

    setTimeout(() => {
      setOpen(false);
    }, 3000);
  };

  function loadMedicalExaminationInHistory() {
    setTextInputs((prev) => ({
      treatmentProcess: h.treatmentProcess,
      symptomProcess: h.symptomProcess,
      advance: h.advance,
      predict: h.predict,
    }));
    setDayExam((prev) => h.durationDay);

    if (precriptionItems.length > 0) {
      const updatedItems = [];
      precriptionItems.map((p) => {
        updatedItems.push({
          id: p.medicine.id,
          name: p.name,
          unitName: p.medicine.unitType.unitName,
          description: p.medicine.description,
          defaultPerDay: p.medicine.defaultPerDay,
          prognosis: p.medicine.defaultPerDay * h.durationDay,
        });
      });

      setMedicinesExamList(updatedItems);
    }
  }

  const getAllMedicines = useCallback(async () => {
    let response;
    if (isBACSI(currentUser) && currentUser !== null) {
      try {
        let url = `${endpoints["getAllMedicines"]}`;
        response = await authAPI().get(url, {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        });

        if (response.status === 200) {
          setAllMedicines(response.data);
        } else showSnackbar(response.data, "error");
      } catch {
        showSnackbar("Lỗi", "error");
      }
    }
  }, []);

  const getAllMedicineGroup = useCallback(async () => {
    let response;
    if (isBACSI(currentUser) && currentUser !== null) {
      try {
        let url = `${endpoints["getAllMedicineGroup"]}`;
        response = await authAPI().get(url, {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        });

        if (response.status === 200) {
          setMedicineGroupList(response.data);
        } else showSnackbar(response.data, "error");
      } catch {
        showSnackbar("Lỗi", "error");
      }
    }
  }, [currentUser]);

  const getAllMedicinesByGroup = async (selectedMGId) => {
    try {
      const response = await authAPI().get(
        endpoints["getAllMedicinesByGroup"](selectedMGId),
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );
      if (response.status === 200) {
        setMedicinesList(response.data);
      } else showSnackbar(response.data, "error");
    } catch {
      showSnackbar("Lỗi", "error");
    }
  };

  const addMedicinesToExamList = useCallback(
    async (selectedMedicineId) => {
      try {
        const response = await authAPI().get(
          endpoints["getMedicineById"](selectedMedicineId),
          {
            validateStatus: function (status) {
              return status < 500; // Chỉ ném lỗi nếu status code >= 500
            },
          }
        );
        if (response.status === 200) {
          const updatedItems = [...medicinesExamList];

          const existingMedicineIndex = updatedItems.findIndex(
            (medicineItem) => medicineItem.id === selectedMedicineId
          );

          const existingMedicine = updatedItems[existingMedicineIndex];

          if (existingMedicine) {
            showSnackbar("Đã chọn loại thuốc này !", "error");
          } else {
            const medicine = response.data;

            updatedItems.push({
              id: medicine.id,
              name: medicine.name,
              unitName: medicine.unitType.unitName,
              description: medicine.description,
              defaultPerDay: medicine.defaultPerDay,
              prognosis: medicine.defaultPerDay * dayExam,
            });
          }
          setMedicinesExamList(updatedItems);
        } else showSnackbar(response.data, "error");
      } catch {
        showSnackbar("Lỗi", "error");
      }
    },
    [selectedMedicineId, medicinesExamList]
  );

  useEffect(() => {
    if (currentUser !== null && medicineGroupList.length < 1) {
      getAllMedicineGroup();
      if (h && precriptionItems) loadMedicalExaminationInHistory();
    }
  }, [currentUser, selectedMedicineId, medicinesExamList, dayExam]);

  function handleSelectedMedicineGroup(mgId) {
    setSelectedMGId(mgId);
    getAllMedicinesByGroup(mgId);
    setSelectedMedicineId(0);
  }

  function handleSelectedMedicine(mId) {
    setSelectedMedicineId(mId);
    addMedicinesToExamList(mId);
  }

  function handleDeleteMedicineExamItem(mId) {
    const updatedItems = [...medicinesExamList];
    const existingMedicineIndex = updatedItems.findIndex(
      (medicineItem) => medicineItem.id === mId
    );
    const existingMedicine = updatedItems[existingMedicineIndex];

    if (existingMedicine) {
      updatedItems.splice(existingMedicineIndex, 1);
      setMedicinesExamList(updatedItems);
    }
  }

  function handleUpdateDescriptionMedicineExamItem(mId, event) {
    const updatedItems = [...medicinesExamList];
    const existingMedicineIndex = updatedItems.findIndex(
      (medicineItem) => medicineItem.id === mId
    );
    const existingMedicine = updatedItems[existingMedicineIndex];

    if (existingMedicine) {
      existingMedicine.description = event.target.value;
      setMedicinesExamList(updatedItems);
    }
  }

  function handleUpdatePrognosisMedicineExamItem(mId, event) {
    const updatedItems = [...medicinesExamList];
    const existingMedicineIndex = updatedItems.findIndex(
      (medicineItem) => medicineItem.id === mId
    );
    const existingMedicine = updatedItems[existingMedicineIndex];

    if (existingMedicine) {
      existingMedicine.prognosis = event.target.value;
      setMedicinesExamList(updatedItems);
    }
  }

  function handleAutoUpdateExamValueValueSetDayExam(dayIndex) {
    setDayExam(dayIndex);
    const updatedItems = [...medicinesExamList];
    if (updatedItems.length > 0) {
      updatedItems.map((ui) => {
        ui.prognosis = ui.defaultPerDay * dayIndex;
      });
      setMedicinesExamList(updatedItems);
    }
  }

  function handleSetDayExam(e) {
    let day = parseInt(e.target.value, 10);
    if (day > 0 && day < 7) {
      handleAutoUpdateExamValueValueSetDayExam(day);
    } else if (day > 31 || day < 1) {
      showSnackbar(
        "Không được cấp quá 31 ngày và phải cấp it nhất 1 ngày !",
        "error"
      );
      setDayExam(1);
      e.target.value = "";
    } else {
      handleAutoUpdateExamValueValueSetDayExam(day);
    }
  }

  function handleSetNewMedicineOpen() {
    setNewMedicineOpen((prev) => !prev);
    if (allMedicines.length < 1) getAllMedicines();
  }

  function handleAddNewMedicineInput(event) {
    const medicineName = event.target.value;

    const updatedItems = [...medicinesExamList];

    const medicineIndex = allMedicines.findIndex(
      (medicineItem) => medicineItem.name === medicineName
    );
    const existingMedicineDatabase = allMedicines[medicineIndex];

    let existingMedicineIndex = null;
    let existingMedicineInExam = null;

    if (existingMedicineDatabase) {
      existingMedicineIndex = updatedItems.findIndex(
        (medicineItem) => medicineItem.id === existingMedicineDatabase.id
      );

      existingMedicineInExam = updatedItems[existingMedicineIndex];
    }

    if (!existingMedicineDatabase) {
      showSnackbar("Loại thuốc này không tồn tại !", "error");
    } else if (existingMedicineInExam) {
      showSnackbar("Đã tồn tại trong đơn thuốc !", "error");
    } else {
      updatedItems.push({
        id: existingMedicineDatabase.id,
        name: existingMedicineDatabase.name,
        unitName: existingMedicineDatabase.unitType.unitName,
        description: existingMedicineDatabase.description,
        defaultPerDay: existingMedicineDatabase.defaultPerDay,
        prognosis: existingMedicineDatabase.defaultPerDay * dayExam,
      });
      setMedicinesExamList(updatedItems);
      setNewMedicineOpen((prev) => false);
    }
  }

  function handleSetTextInputs(e) {
    const { name, value } = e.target;
    setTextInputs((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  const handleSubmitMedicalExamination = async (e) => {
    e.preventDefault();

    setLoading(true);
    const filteredMedicines = medicinesExamList.map((medicine) => ({
      id: medicine.id,
      description: medicine.description,
      prognosis: medicine.prognosis,
    }));
    try {
      const response = await authAPI().post(
        endpoints["submitMedicalExamination"],
        {
          mrlId: examPatient.id,
          advance: textInputs.advance,
          predict: textInputs.predict,
          symptomProcess: textInputs.symptomProcess,
          treatmentProcess: textInputs.treatmentProcess,
          followUpDate: textInputs.followUpDate,
          durationDay: dayExam,
          medicinesExamList: filteredMedicines,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );
      if (response.status === 201) {
        showSnackbar("Lưu phiếu khám thành công", "success");
        setTimeout(() => {
          setLoading(false);
          navigate("/prepare-examination-form");
        }, 3000);
      } else showSnackbar(response.data, "error");
    } catch {
      showSnackbar("Lỗi", "error");
    }
  };

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />
      <div className="examination-form-container">
        <h1 className="text text-danger text-center mb-4">LẬP PHIẾU KHÁM</h1>
        <form onSubmit={(e) => handleSubmitMedicalExamination(e)}>
          <div className="row">
            {/* Cột bên trái */}
            <div className="col-lg-5">
              {/* Hàng đầu tiên của 2 input */}
              <div className="row mb-5">
                <div className="col">
                  <div className="input-card">
                    <div className="input-card-header">
                      <h4>Quá trình điều trị</h4>
                    </div>
                    <div className="input-card-content">
                      <textarea
                        required
                        className="w-100"
                        rows="5"
                        placeholder="Nhập nội dung..."
                        value={textInputs.treatmentProcess}
                        name="treatmentProcess"
                        onChange={(e) => handleSetTextInputs(e)}
                      ></textarea>
                    </div>
                  </div>
                </div>
                <div className="col">
                  <div className="input-card">
                    <div className="input-card-header">
                      <h4>Diễn biến bệnh</h4>
                    </div>
                    <div className="input-card-content">
                      <textarea
                        required
                        className="w-100"
                        rows="5"
                        placeholder="Nhập nội dung..."
                        value={textInputs.symptomProcess}
                        name="symptomProcess"
                        onChange={(e) => handleSetTextInputs(e)}
                      ></textarea>
                    </div>
                  </div>
                </div>
              </div>

              {/* Hàng thứ hai của 2 bảng */}
              <div className="row">
                <div className="col">
                  <div className="medicine-group-container selection-card">
                    <div className="selection-card-header">
                      <h4 className="text-center">Nhóm thuốc</h4>
                    </div>
                    <div className="selection-card-content">
                      <ul className="selection-list">
                        {medicineGroupList.length > 1 &&
                          medicineGroupList.map((mg) => {
                            const mgId = mg.id;
                            return (
                              <>
                                <li
                                  className={
                                    selectedMGId === mg.id ? "selected" : ""
                                  }
                                  key={mg.id}
                                  onClick={() =>
                                    handleSelectedMedicineGroup(mgId)
                                  }
                                >
                                  {mg.groupName}
                                </li>
                              </>
                            );
                          })}
                      </ul>
                    </div>
                  </div>
                </div>
                <div className="col">
                  <div className="medicine-group-container selection-card">
                    <div className="selection-card-header">
                      <h4 className="text-center">Danh sách thuốc</h4>
                    </div>
                    <div className="selection-card-content">
                      <ul className="selection-list">
                        {medicinesList.length > 0 &&
                          medicinesList.map((m) => {
                            return (
                              <>
                                <li
                                  key={m.id}
                                  className={
                                    selectedMedicineId === m.id
                                      ? "selected text-center"
                                      : "text-center"
                                  }
                                  onClick={() => handleSelectedMedicine(m.id)}
                                >
                                  {m.name}
                                </li>
                              </>
                            );
                          })}
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Cột bên phải */}
            <div className="col-lg-7">
              <div className="border p-4 shadow bg-white large-box">
                <div className="examination-container">
                  <div className="prescription-header">
                    <h2>ĐƠN THUỐC</h2>
                  </div>

                  <div className="prescription-details">
                    <p>
                      <span>Họ tên:</span> {examPatient?.name}
                    </p>
                    <p>
                      <span>Nhu cầu khám : </span>
                      {examPatient?.favor}
                    </p>
                  </div>
                  <p>
                    <span>Chẩn đoán:</span>{" "}
                    <input
                      type="text"
                      required
                      value={textInputs.predict}
                      name="predict"
                      onChange={(e) => handleSetTextInputs(e)}
                    />
                  </p>

                  <div className="btn-group" role="group">
                    {Array.from({ length: 6 }, (_, i) => (
                      <button
                        type="button"
                        className={`btn btn-outline-${
                          dayExam === i + 1
                            ? "success btn-success text text-white"
                            : "secondary"
                        }`}
                        onClick={() =>
                          handleAutoUpdateExamValueValueSetDayExam(i + 1)
                        }
                        key={i + 1}
                      >
                        {i + 1} Ngày
                      </button>
                    ))}
                    <button
                      type="button"
                      className={`btn btn-outline-${
                        dayExam > 6
                          ? "success btn-success text text-white"
                          : "secondary"
                      }`}
                    >
                      <input
                        min={0}
                        max={30}
                        style={{ width: 40 + "%" }}
                        type="number"
                        defaultValue={
                          h !== undefined && h.durationDay
                            ? h.durationDay
                            : dayExam
                        }
                        onBlur={(e) => {
                          if (e.target.value.trim() !== "") handleSetDayExam(e);
                          else {
                            e.target.value = 1;
                            handleSetDayExam(e);
                          }
                        }}
                      />
                      Ngày
                    </button>
                  </div>

                  <div className="medicine-list-container container mt-4">
                    {medicinesExamList.length > 0 &&
                      medicinesExamList.map((m, index) => {
                        return (
                          <>
                            <div
                              key={m.id}
                              className="row align-items-center border-bottom py-2"
                            >
                              <div className="col-1">
                                <strong>{index + 1}.</strong>
                              </div>
                              <div className="col-6">
                                <strong>{m.name}</strong>
                                <p>
                                  Cách dùng :{" "}
                                  <input
                                    type="text"
                                    value={m.description}
                                    onChange={(e) =>
                                      handleUpdateDescriptionMedicineExamItem(
                                        m.id,
                                        e
                                      )
                                    }
                                  />
                                </p>
                              </div>
                              <div className="col-4 text-end">
                                <strong>Số lượng</strong>
                                <div className="col-12 text-end">
                                  <strong>
                                    <input
                                      className="text-center"
                                      type="text"
                                      value={m.prognosis}
                                      style={{ width: 20 + "%" }}
                                      onChange={(e) =>
                                        handleUpdatePrognosisMedicineExamItem(
                                          m.id,
                                          e
                                        )
                                      }
                                    />{" "}
                                    {m.unitName}
                                  </strong>
                                </div>
                              </div>
                              <div className="col-1 text-end">
                                <button
                                  onClick={() =>
                                    handleDeleteMedicineExamItem(m.id)
                                  }
                                  type="button"
                                  className="btn-close"
                                  aria-label="Close"
                                ></button>
                              </div>
                            </div>
                          </>
                        );
                      })}
                    <div className="col-12 text-end">
                      <button
                        onClick={() => handleSetNewMedicineOpen()}
                        type="button"
                        className={`btn-open text-white border border-none bg-${
                          newMedicineOpen === true ? "danger" : "success"
                        }`}
                        aria-label="Add"
                      >
                        {newMedicineOpen === true ? "X" : "+"}
                      </button>
                    </div>
                    {newMedicineOpen === true && (
                      <Typeahead
                        id="basic-typeahead"
                        labelKey="name"
                        options={allMedicines}
                        placeholder="Thêm thuốc mới vào đơn"
                        onBlur={(e) => {
                          if (e.target.value.trim() !== "")
                            handleAddNewMedicineInput(e);
                        }}
                      />
                    )}
                  </div>

                  <div className="footer-examination-form">
                    <div className="left-col">
                      <label>Lời dặn :</label>
                      <div className="">
                        <textarea
                          required
                          className="textarea-advance"
                          value={textInputs.advance}
                          name="advance"
                          onChange={(e) => handleSetTextInputs(e)}
                        ></textarea>
                      </div>
                      <div className="">
                        <label>Ngày tái khám :</label>
                        <input
                          type="date"
                          value={textInputs.followUpDate}
                          name="followUpDate"
                          onChange={(e) => handleSetTextInputs(e)}
                        />
                      </div>
                    </div>

                    <div className="right-col">
                      <div className="date">
                        <strong>
                          {dayjs(examPatient.schedule.date).format(
                            "DD/MM/YYYY"
                          )}
                        </strong>
                      </div>

                      <p className="doctor-title">BÁC SĨ</p>
                      <p className="doctor-name">{currentUser.name}</p>
                    </div>
                  </div>
                  <div className="d-flex justify-content-center align-item-center mt-5">
                    {loading ? (
                      <div className="d-flex justify-content-center align-item-center">
                        <CircularProgress className="mt-3" />
                      </div>
                    ) : (
                      <button
                        type="submit"
                        className="btn btn-danger text-center"
                      >
                        Lưu
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </>
  );
}
