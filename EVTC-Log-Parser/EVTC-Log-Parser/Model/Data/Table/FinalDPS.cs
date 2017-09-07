namespace EVTC_Log_Parser.Model
{
    public class FinalDPS
    {
        public string Group { get; set; }
        public string Character { get; set; }
        public string Profession { get; set; }
        public int Power { get; set; }
        public int Condi { get; set; }
        public int Total { get { return Power + Condi; } }
        public double DPS { get; set; }

        public string[] ToStringArray()
        {
            return new string[] { Group, Character, Profession, Power.ToString(), Condi.ToString(), Total.ToString(), DPS.ToString() };
        }

    }
}
