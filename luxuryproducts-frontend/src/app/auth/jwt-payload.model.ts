export class JwtPayload {
  public sub: string;
  public email: string;
  public authority: string;
  public exp: number;
  public iat: number;
}
