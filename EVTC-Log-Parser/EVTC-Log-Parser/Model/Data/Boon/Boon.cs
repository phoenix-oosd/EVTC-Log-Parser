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
                yield return Might;
                yield return Fury;
                yield return Quickness;
                yield return Alacrity;

                yield return EmpowerAllies;
                yield return BannerOfStrength;
                yield return BannerOfDiscipline;

                yield return Spotter;
                yield return SunSpirit;
                yield return FrostSpirit;
                yield return GlyphOfEmpowerment;
                yield return GraceOfTheLand;

                yield return PinpointDistribution;

                yield return AssassinsPresence;
                yield return NaturalisticResonance;
            }
        }
        #endregion
    }
}
