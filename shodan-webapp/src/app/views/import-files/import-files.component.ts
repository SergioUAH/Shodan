import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxFileDropEntry, FileSystemFileEntry, FileSystemDirectoryEntry } from 'ngx-file-drop';
import { RestService } from 'src/app/config/services/rest-service';
import { environment } from 'src/environments/environment';

const API_IMPORT_WORDLIST = "/files/importWordlist";

@Component({
  selector: 'app-import-files',
  templateUrl: './import-files.component.html',
  styleUrls: ['./import-files.component.scss']
})
export class ImportFilesComponent implements OnInit {

  wordlist: File = null;
  placeholder: string = 'Elija un archivo o arrástrelo aquí.';
  fileName: string = '';
  loading = false;
  saveDisabled = true;

  constructor(public http: RestService, private cdr: ChangeDetectorRef) { }

  ngOnInit() {
  }

  uploadFile() {
    this.loading = true;
    this.http.postToImportFile(environment.url + API_IMPORT_WORDLIST, this.wordlist).subscribe(data => {
      this.loading = false;
      this.cdr.markForCheck();
	}, error => {
      console.log("ERROR");
      console.log(error.message);
      this.loading = false;
      this.cdr.markForCheck();
    });
  }

  public files: NgxFileDropEntry[] = [];

  public handleFileInput(files: NgxFileDropEntry[]) {
    this.files = files;
    for (const droppedFile of files) {
      if (droppedFile.fileEntry.isFile) {
        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        fileEntry.file((file: File) => {
          this.wordlist = file;
          this.placeholder = this.wordlist.name;
          this.saveDisabled = false;
          this.cdr.markForCheck();
        });
      } else {
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

  public fileOver(event) {
    console.log(event);
  }

  public fileLeave(event) {
    console.log(event);
  }

}
