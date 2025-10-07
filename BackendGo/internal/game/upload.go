package game

import (
	"io"
	"net/http"
	"os"
)

func UploadHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseMultipartForm(10 << 20)
	file, handler, err := r.FormFile("icon")
	if err != nil {
		http.Error(w, "Error fetching image", http.StatusBadRequest)
		return
	}
	defer file.Close()
	os.MkdirAll("uploads", os.ModePerm)

	dst, err := os.Create("uploads/" + handler.Filename)
	if err != nil {
		http.Error(w, "Unable to save file", http.StatusInternalServerError)
		return
	}
	defer dst.Close()

	io.Copy(dst, file)
	w.WriteHeader(http.StatusOK)
}