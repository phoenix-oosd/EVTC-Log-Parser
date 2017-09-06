namespace EVTC_Log_Parser.Model
{
    public class StateChangeEvent
    {
        #region Properties
        public int Time { get; set; }
        public StateChange StateChange { get; set; }
        #endregion
    }
}
