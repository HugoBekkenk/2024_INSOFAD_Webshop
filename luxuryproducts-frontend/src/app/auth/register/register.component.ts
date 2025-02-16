import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {AuthResponse} from "../auth-response.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  public registerForm: FormGroup;
  public loginCompleted: boolean = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      infix: [''],
      lastName: ['', Validators.required],
      email: ['', [Validators.email, Validators.required, Validators.maxLength(64), Validators.minLength(5)]],
      password: ['', [Validators.minLength(8), Validators.maxLength(128)]],
      repeated_password: ['']
    });
  }

  public onSubmit(): void {
    let newRegisterForm = this.registerForm.value
    var formatSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/
    var formatLower = /[a-z]/
    var formatUpper = /[A-Z]/
    var formatNumber = /\d/
    if (newRegisterForm.password === newRegisterForm.repeated_password){
      if (formatSpecial.test(newRegisterForm.password) ){
        if (formatUpper.test(newRegisterForm.password)){
          if (formatLower.test(newRegisterForm.password)){
            if (formatNumber.test(newRegisterForm.password)){
              this.authService
              .register(this.registerForm.value)
              .subscribe((authReponse: AuthResponse) => {
                this.loginCompleted = true
                this.router.navigate(['']);
              }, error => {
                console.log(error.status);
                if (error.status === 404){
                  alert("This email already exist in our system");
                } else{
                  alert("This email is invalid.");
                }
              });
            } else{
              alert("Password must contain a number");
          }
          }else {
            alert("Password must contain lowercase letter(s)");          }
        } else{
          alert("Password must contain uppercase letter(s)");        }
    } else{
      alert("Password must contain a special character");    }
    } else{
      alert("Password must be the same as repeated password.")
    }
  }
}
