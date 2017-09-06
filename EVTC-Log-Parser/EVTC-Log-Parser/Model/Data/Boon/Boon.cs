using System.Collections.Generic;

namespace EVTC_Log_Parser.Model
{
    public class Boon
    {
        #region Static
        // Boon
        public static readonly Boon Might = new Boon(740, 25, false);
        public static readonly Boon Fury = new Boon(725, 9, true);
        public static readonly Boon Quickness = new Boon(1187, 5, true);

        // Mesmer
        public static readonly Boon Alacrity = new Boon(30328, 9, true);

        // Guardian
        // Warrior
        public static readonly Boon EmpowerAllies = new Boon(14222, 1, true);
        public static readonly Boon BannerOfStrength = new Boon(14417, 1, true);
        public static readonly Boon BannerOfDiscipline = new Boon(14449, 1, true);
    
        // Ranger
        public static readonly Boon Spotter = new Boon(14055, 1, true);
        public static readonly Boon SunSpirit = new Boon(12540, 1, true);
        public static readonly Boon FrostSpirit = new Boon(12544, 1, true);
        public static readonly Boon GlyphOfEmpowerment = new Boon(31803, 1, true);
        public static readonly Boon GraceOfTheLand = new Boon(34062, 5, false);

        // Engineer
        public static readonly Boon PinpointDistribution = new Boon(38333, 1, true);

        // Necromancer
        // Thief
        // Elementalist
        // Revenant
        public static readonly Boon AssassinsPresence = new Boon(26854, 1, true);
        public static readonly Boon NaturalisticResonance = new Boon(29379, 1, true);
        #endregion

        #region Members
        private readonly int _skillId;
        private readonly int _capacity;
        private readonly bool _isDuration;
        #endregion

        #region Properties
        public int SkillId { get { return _skillId; } }
        public int Capacity { get { return _capacity; } }
        public bool IsDuration { get { return _isDuration; } }
        #endregion

        #region Constructor
        private Boon(int skillId, int capacity, bool isDuration)
        {
            _skillId = skillId;
            _capacity = capacity;
            _isDuration = isDuration;
        }
        #endregion

        #region Public Methods
        public static IEnumerable<Boon> Values
        {
            get
            {
                if (Properties.Settings.Default.Might) { yield return Might; }
                if (Properties.Settings.Default.Fury) { yield return Fury; }
                if (Properties.Settings.Default.Quickness) { yield return Quickness; }

                if (Properties.Settings.Default.Alacrity) { yield return Alacrity; }

                if (Properties.Settings.Default.EmpowerAllies) { yield return EmpowerAllies;}
                if (Properties.Settings.Default.BannerOfStrength) { yield return BannerOfStrength; }
                if (Properties.Settings.Default.BannerOfDiscipline) { yield return BannerOfDiscipline; }

                if (Properties.Settings.Default.Spotter) { yield return Spotter; }
                if (Properties.Settings.Default.SunSpirit) { yield return SunSpirit; }
                if (Properties.Settings.Default.FrostSpirit) { yield return FrostSpirit; }
                if (Properties.Settings.Default.GlyphOfEmpowerment) { yield return GlyphOfEmpowerment; }
                if (Properties.Settings.Default.GraceOfTheLand) { yield return GraceOfTheLand; }

                if (Properties.Settings.Default.PinpointDistribution) { yield return PinpointDistribution; }

                if (Properties.Settings.Default.AssassinsPresence) { yield return AssassinsPresence; }
                if (Properties.Settings.Default.NaturalisticResonance) { yield return NaturalisticResonance; }
            }
        }
        #endregion
    }
}
